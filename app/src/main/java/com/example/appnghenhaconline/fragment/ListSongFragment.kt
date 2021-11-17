package com.example.appnghenhaconline.fragment

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.adapter.SongAdapter
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.models.playlist.Playlist
import com.example.appnghenhaconline.models.song.DataSong
import com.example.appnghenhaconline.models.song.Song
import com.example.appnghenhaconline.dataLocalManager.Service.MyService
import com.example.appnghenhaconline.dataLocalManager.SharedPreferences.SessionUser
import com.example.appnghenhaconline.models.user.DataUser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class ListSongFragment: Fragment() {

    internal lateinit var view: View
    lateinit var rcvSong: RecyclerView
    lateinit var tittleAlbum : TextView
    lateinit var imgAlbum : ImageView
    lateinit var listsong : ArrayList<Song>
    lateinit var idPlayList : String
    lateinit var mediaPlayer : MediaPlayer
    lateinit var songAdapter: SongAdapter
    private lateinit var btnFollow: ImageView
    var isFollow: Boolean = false
    lateinit var sessionUser : SessionUser

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fm_album_music_fragment, container, false)
        return view
    }

    override fun onStart() {
        super.onStart()
        sessionUser = SessionUser(this.requireContext())
        init()
        checkFollowed()
    }

    private fun init(){
        tittleAlbum = view.findViewById(R.id.tittleAlbumMusic)
        imgAlbum = view.findViewById(R.id.imgAlbumMusic)
        btnFollow = view.findViewById(R.id.btnFollow)
        initPlaylist()
        initSongList()
        event()
    }

    private fun event() {
//        btnAddPlaylist.setOnClickListener {
//            val selectedSongs = songAdapter.getSelectedSong()
//
//            val showSongs = StringBuffer()
//            for (i in 0 until selectedSongs.size){
//                if (i == 0){
//                    showSongs.append(selectedSongs[i].title)
//                }else{
//                    showSongs.append("\n").append(selectedSongs[i].title)
//                }
//            }
//            MyLib.showToast(requireContext(), showSongs.toString())
//        }
        clickFollow()
    }

    private fun initSongList(){
        //khởi tạo danh sách bài hát
        listsong = ArrayList()
        songAdapter = SongAdapter(view.context,listsong)

        rcvSong = view.findViewById(R.id.rcvSong)
        rcvSong.setHasFixedSize(true)
        rcvSong.layoutManager = LinearLayoutManager(view.context,
                                LinearLayoutManager.VERTICAL,false)
        rcvSong.adapter = songAdapter
        //Sự kiện onItemClick
        songAdapter.setOnItemClickListener(object : SongAdapter.IonItemClickListener{
            override fun onItemClick(position: Int) {
                if (mediaPlayer.isPlaying){
                    mediaPlayer.stop()
                    clickStartService(listsong, position)
                }else{
                    clickStartService(listsong, position)
                }
            }
        })

        callApiShowListSongByID(listsong,songAdapter,idPlayList)
        mediaPlayer = MediaPlayer()
    }

    private fun initPlaylist(){
        // Nhận dữ liệu playlist từ PlayNowFragment
        val bundleReceive : Bundle = requireArguments()
        val playlist : Playlist = bundleReceive["object_song"] as Playlist

        tittleAlbum.text = playlist.playlistname
        idPlayList = playlist.id // get id of playlist
        Picasso.get().load(playlist.image)
            .resize(800,800)
            .into(imgAlbum)

    }

    private fun callApiShowListSongByID(songs : ArrayList<Song>,songAdapter : SongAdapter, id : String ) {
        
        ApiService.apiService.getListSongByID(id).enqueue(object : Callback<DataSong?> {
            override fun onResponse(call: Call<DataSong?>, response: Response<DataSong?>) {
                val dataSong = response.body()
                MyLib.showLog(dataSong.toString())
                if(dataSong!=null){
                    if(!dataSong.error){
                        val listSong: ArrayList<Song> = dataSong.listSong

                        MyLib.showLog(listSong.toString())

                        songs.addAll(listSong)

                        songAdapter.notifyDataSetChanged()
                    }else MyLib.showLog(dataSong.message)
                }
            }

            override fun onFailure(call: Call<DataSong?>, t: Throwable) {
                MyLib.showLog(t.toString())
            }
        })
    }

    private fun clickStopService() {
        val intent = Intent(requireContext(), MyService::class.java)
        activity?.stopService(intent)
    }

    //call service xử lý xự kiện phát nhạc
    private fun clickStartService(list : ArrayList<Song>, position: Int) {
        val intent = Intent(requireContext(), MyService::class.java)

        val bundle = Bundle()
        bundle.putSerializable("position_song", position)
        bundle.putSerializable("list_song", list)
        intent.putExtras(bundle)

        activity?.startService(intent)
    }

    private fun checkFollowed () {
        var getUser = sessionUser.getUserDetails()
        var gson = Gson()
        val type: Type = object : TypeToken<ArrayList<Playlist?>?>() {}.type
        var playlist : ArrayList<Playlist> = gson.fromJson(getUser[sessionUser.KEY_PLAYLIST],type)

        for (i in playlist) {
            if (i.id == idPlayList) {
                isFollow = true
                btnFollow.setImageResource(R.drawable.ic_favorite_selected)
            }
        }
    }

    private fun clickFollow() {
        val getUser = sessionUser.getUserDetails()
        btnFollow.setOnClickListener {

            if (!isFollow){
                isFollow = true
                btnFollow.setImageResource(R.drawable.ic_favorite_selected)
                callApiFollowOrUnfollow(getUser[sessionUser.KEY_ID]!!,idPlayList,isFollow)

            }

            else{
                isFollow = false
                btnFollow.setImageResource(R.drawable.ic_favorite)
                callApiFollowOrUnfollow(getUser[sessionUser.KEY_ID]!!,idPlayList,isFollow)
            }

        }
    }

    private fun callApiFollowOrUnfollow(userId : String, playlistId : String, status : Boolean ) {
        ApiService.apiService.followOrUnfollowPlayList(userId,playlistId,status).enqueue(object : Callback<DataUser?> {
            override fun onResponse(call: Call<DataUser?>, response: Response<DataUser?>) {

                val followStatus = response.body()
                MyLib.showLog(followStatus.toString())
                if(followStatus!=null){
                    if (!followStatus.error) {
                        MyLib.showToast(requireContext(),followStatus.message)
                        var gson = Gson()
                        var listPlaylist = gson.toJson(followStatus.user.followPlaylist)
                        sessionUser.editor.putString(sessionUser.KEY_PLAYLIST,listPlaylist)
                        sessionUser.editor.commit()
                    }
                    else {
                        MyLib.showToast(requireContext(),followStatus.message)

                    }
                }
            }

            override fun onFailure(call: Call<DataUser?>, t: Throwable) {
                MyLib.showToast(requireContext(),"Call Api Error")
            }
        })
    }
}


