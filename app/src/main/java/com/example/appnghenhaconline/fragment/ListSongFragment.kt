package com.example.appnghenhaconline.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.adapter.SongAdapter
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.adapter.PlaylistSelectedAdapter
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.models.playlist.Playlist
import com.example.appnghenhaconline.models.song.DataSong
import com.example.appnghenhaconline.models.song.Song
import com.example.appnghenhaconline.dataLocalManager.Service.MyService
import com.example.appnghenhaconline.dataLocalManager.SharedPreferences.SessionUser
import com.example.appnghenhaconline.models.playlist.DataPlayList
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
    lateinit var listPlaylistSelected : ArrayList<Playlist>
    lateinit var idPlayList : String
    lateinit var mediaPlayer : MediaPlayer
    lateinit var songAdapter: SongAdapter
    lateinit var playlistSelectedAdapter: PlaylistSelectedAdapter
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
        clickFollow()
    }

    //===========================================================
    //region INIT ADAPTER
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
            override fun onItemSelected(position: Int) {
                openDialogAddPlaylist(Gravity.CENTER)
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

    private fun initListPlaylist(rcvPlaylistSelected: RecyclerView) {
        listPlaylistSelected = ArrayList()
        playlistSelectedAdapter = PlaylistSelectedAdapter(requireContext(),listPlaylistSelected)

        rcvPlaylistSelected.setHasFixedSize(true)
        rcvPlaylistSelected.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        rcvPlaylistSelected.adapter = playlistSelectedAdapter

        playlistSelectedAdapter.setOnItemClickListener(object : PlaylistSelectedAdapter.IonItemClickListener{
            override fun onItemClick(position: Int) {
                MyLib.showToast(requireContext(), listPlaylistSelected[position].playlistname)
            }
        })
        callApiPlayList(listPlaylistSelected, playlistSelectedAdapter)
    }
    //endregion
    //===========================================================
    //region ANOTHER FUNCTION

    // mở dialog thêm vào playlist
    private fun openDialogAddPlaylist(gravity: Int){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dlg_select_playlist_dialog)
        dialog.setCancelable(true)

        val window = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val windowAttributes: WindowManager.LayoutParams = window!!.attributes
        windowAttributes.gravity = gravity
        window.attributes = windowAttributes

        val btnExit: Button = dialog.findViewById(R.id.btnExit)
        val rcvPlaylistSelected: RecyclerView = dialog.findViewById(R.id.rcvPlaylistSelect)

        initListPlaylist(rcvPlaylistSelected)

        btnExit.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
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

    // kiểm tra follow
    private fun checkFollowed () {
        val getUser = sessionUser.getUserDetails()
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<Playlist?>?>() {}.type
        val playlist : ArrayList<Playlist> = gson.fromJson(getUser[sessionUser.KEY_PLAYLIST],type)

        for (i in playlist) {
            if (i.id == idPlayList) {
                isFollow = true
                btnFollow.setImageResource(R.drawable.ic_favorite_selected)
            }
        }
    }

    // sự kiện click để follow
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
    //endregion
    //===========================================================
    //region CALL API

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

    private fun callApiPlayList(list : ArrayList<Playlist>, adapter : PlaylistSelectedAdapter) {
        ApiService.apiService.getPlayList().enqueue(object : Callback<DataPlayList?> {
            override fun onResponse(call: Call<DataPlayList?>, response: Response<DataPlayList?>) {
                val dataPlayList = response.body()
                if(dataPlayList != null) {
                    if(!dataPlayList.error) {
                        list.addAll(dataPlayList.listPlayList)
                        adapter.notifyDataSetChanged()
                    }
                    else {
                        MyLib.showLog("PlayNowFragment.kt: " + dataPlayList.message)
                    }
                }
            }

            override fun onFailure(call: Call<DataPlayList?>, t: Throwable) {
                MyLib.showToast(requireContext(),"Call Api Error")
            }
        })
    }

    private fun callApiFollowOrUnfollow(userId : String, playlistId : String, status : Boolean ) {
        ApiService.apiService.followOrUnfollowPlayList(userId,playlistId,status).enqueue(object : Callback<DataUser?> {
            override fun onResponse(call: Call<DataUser?>, response: Response<DataUser?>) {

                val followStatus = response.body()
                MyLib.showLog(followStatus.toString())
                if(followStatus!=null){
                    if (!followStatus.error) {
                        MyLib.showToast(requireContext(),followStatus.message)
                        val gson = Gson()
                        val listPlaylist = gson.toJson(followStatus.user.followPlaylist)
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
    //endregion





}


