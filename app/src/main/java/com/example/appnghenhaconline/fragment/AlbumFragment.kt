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
import com.airbnb.lottie.LottieAnimationView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.adapter.SongAdapter
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.activity.PlayMusicActivity.Companion.isShuffle
import com.example.appnghenhaconline.adapter.PlaylistSelectedAdapter
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.dataLocalManager.MyDataLocalManager
import com.example.appnghenhaconline.models.playlist.Playlist
import com.example.appnghenhaconline.models.song.DataSong
import com.example.appnghenhaconline.models.song.Song
import com.example.appnghenhaconline.dataLocalManager.Service.MyService
import com.example.appnghenhaconline.dataLocalManager.SharedPreferences.SessionUser
import com.example.appnghenhaconline.models.album.Album
import com.example.appnghenhaconline.models.playlist.DataPlayListUser
import com.example.appnghenhaconline.models.playlist.PlayListUser
import com.example.appnghenhaconline.models.user.DataUser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.*
import kotlin.collections.ArrayList

class AlbumFragment: Fragment() {

    internal lateinit var view: View
    private lateinit var rcvSong: RecyclerView
    private lateinit var tittleAlbum : TextView
    private lateinit var imgAlbum : ImageView
    private lateinit var listsong : ArrayList<Song>
    private lateinit var listPlaylistSelected : ArrayList<PlayListUser>
    private lateinit var playlistSelectedAdapter: PlaylistSelectedAdapter
    private lateinit var idAlbum : String
    private var mediaPlayer : MediaPlayer = MediaPlayer()
    private lateinit var songAdapter: SongAdapter
    private lateinit var sessionUser : SessionUser
    private lateinit var lavFollow : LottieAnimationView
    private lateinit var btnPlayPlaylist : Button
    private lateinit var btnShufflePlaylist : Button
    private lateinit var btnBack: ImageView
    private var isFollow: Boolean = false

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
        lavFollow = view.findViewById(R.id.lavFollow)
        btnPlayPlaylist = view.findViewById(R.id.btnPlayPlaylist)
        btnShufflePlaylist = view.findViewById(R.id.btnShufflePlaylist)
        btnBack = view.findViewById(R.id.btnBack)
        initPlaylist()
        initSongList()
        event()
    }

    private fun event() {
        clickFollow()
        btnBack.setOnClickListener {
            val fragmentLayout = PlayNowFragment()
            MyLib.changeFragment(requireActivity(), fragmentLayout)
        }
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
                openDialogAddPlaylist(Gravity.CENTER, listsong[position].id)
            }
        })

        btnPlayPlaylist.setOnClickListener {
            isShuffle = false
            if (mediaPlayer.isPlaying){
                mediaPlayer.stop()
                mediaPlayer.release()
                clickStartService(listsong,position = 0)
            }else{
                clickStartService(listsong,position = 0)
            }
            MyDataLocalManager.setIsShuffle(isShuffle)
        }

        btnShufflePlaylist.setOnClickListener {
            isShuffle = true
            if (mediaPlayer.isPlaying){
                mediaPlayer.stop()
                mediaPlayer.release()
                sendActionToService(MyService.ACTION_NEXT)
            }else{
                clickStartService(listsong,position = 0)
                sendActionToService(MyService.ACTION_NEXT)
            }
//            clickStartService(listsong, randomPosition(listsong.size-1))
//            sendActionToService(MyService.ACTION_NEXT)
            MyDataLocalManager.setIsShuffle(isShuffle)
        }
        callApiShowListSongByAlbumID(listsong,songAdapter,idAlbum)
    }

    private fun randomPosition(i: Int): Int{
        val random = Random()
        return random.nextInt(i + 1)
    }

    private fun sendActionToService(action: Int){
        val intent = Intent(requireContext(), MyService::class.java)
        intent.putExtra("action_music_service", action)
        requireActivity().startService(intent)
    }

    private fun initPlaylist(){
        // Nhận dữ liệu playlist từ PlayNowFragment
        val bundleReceive : Bundle = requireArguments()
        val album : Album = bundleReceive["object_album"] as Album

        tittleAlbum.text = album.albumname
        idAlbum = album.id // get id of playlist
        Picasso.get().load(album.imageAlbum)
            .resize(800,800)
            .into(imgAlbum)

    }

    //khởi tạo danh sách playlist của tôi
    private fun initListPlaylist(rcvPlaylistSelected: RecyclerView, idSong: String) {
        listPlaylistSelected = ArrayList()
        playlistSelectedAdapter = PlaylistSelectedAdapter(requireContext(),listPlaylistSelected)

        rcvPlaylistSelected.setHasFixedSize(true)
        rcvPlaylistSelected.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        rcvPlaylistSelected.adapter = playlistSelectedAdapter

        playlistSelectedAdapter.setOnItemClickListener(object : PlaylistSelectedAdapter.IonItemClickListener{
            override fun onItemClick(position: Int) {
                callApiAddPlaylistUser(listPlaylistSelected[position].id, idSong)
            }
        })
        val user = sessionUser.getUserDetails()
        callApiLoadPlayListUser(listPlaylistSelected,playlistSelectedAdapter, user[sessionUser.KEY_ID]!!)
    }
    //endregion
    //===========================================================
    //region ANOTHER FUNCTION

    // mở dialog thêm vào playlist
    private fun openDialogAddPlaylist(gravity: Int, idSong: String){
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

        initListPlaylist(rcvPlaylistSelected, idSong)

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
        val type: Type = object : TypeToken<ArrayList<Album?>?>() {}.type
        val album : ArrayList<Album> = gson.fromJson(getUser[sessionUser.KEY_ALBUM],type)

        for (i in album) {
            if (i.id == idAlbum) {
                isFollow = true
                lavFollow.setMinAndMaxProgress(1f, 1f)
                lavFollow.speed = 1f
                lavFollow.playAnimation()
//                btnFollow.setImageResource(R.drawable.ic_favorite_selected)
            }
        }
    }

    // sự kiện click để follow
    private fun clickFollow() {
        val getUser = sessionUser.getUserDetails()
        lavFollow.setOnClickListener {

            if (!isFollow){  // đang không follow
                isFollow = true
                lavFollow.setMinAndMaxProgress(0.4f, 1f)
                lavFollow.speed = 1f
                lavFollow.playAnimation()
//                btnFollow.setImageResource(R.drawable.ic_favorite_selected)
                callApiFollowOrUnfollowAlbum(getUser[sessionUser.KEY_ID]!!,idAlbum,isFollow)

            }

            else{ // đang follow
                isFollow = false
                lavFollow.setMinAndMaxProgress(0f, 0.4f)
                lavFollow.speed = -1f
                lavFollow.playAnimation()
//                btnFollow.setImageResource(R.drawable.ic_favorite)
                callApiFollowOrUnfollowAlbum(getUser[sessionUser.KEY_ID]!!,idAlbum,isFollow)
            }

        }
    }
    //endregion
    //===========================================================
    //region CALL API

    private fun callApiShowListSongByAlbumID(songs : ArrayList<Song>,songAdapter : SongAdapter, id : String ) {
        
        ApiService.apiService.getListSongByAlbumId(id).enqueue(object : Callback<DataSong?> {
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

    private fun callApiFollowOrUnfollowAlbum(userId : String, albumId : String, status : Boolean ) {
        ApiService.apiService.followOrUnfollowALbum(userId,albumId,status).enqueue(object : Callback<DataUser?> {
            override fun onResponse(call: Call<DataUser?>, response: Response<DataUser?>) {

                val followStatus = response.body()
                MyLib.showLog(followStatus.toString())
                if(followStatus!=null){
                    if (!followStatus.error) {
                        MyLib.showToast(requireContext(),followStatus.message)
                        val gson = Gson()
                        val listAlbum = gson.toJson(followStatus.user.followAlbum)
                        sessionUser.editor.putString(sessionUser.KEY_ALBUM,listAlbum)
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

    private fun callApiLoadPlayListUser(list : ArrayList<PlayListUser>, adapter : PlaylistSelectedAdapter, idUser : String) {
        ApiService.apiService.loadPlaylistUser(idUser).enqueue(object : Callback<DataPlayListUser?> {
            override fun onResponse(call: Call<DataPlayListUser?>, response: Response<DataPlayListUser?>) {
                val dataPlayList = response.body()
                if(dataPlayList != null) {
                    if (!dataPlayList.error) {
                        val dataPlayListUser = dataPlayList.listPlayListUser

                        list.addAll(dataPlayListUser)
                        adapter.notifyDataSetChanged()
                    }
                    else {
                        MyLib.showToast(requireContext(),dataPlayList.message)
                    }
                }
            }

            override fun onFailure(call: Call<DataPlayListUser?>, t: Throwable) {
                MyLib.showToast(requireContext(),"Call Api Error" )
            }
        })
    }

    private fun callApiAddPlaylistUser(idPlayList : String, idSong : String) {
        ApiService.apiService.addPlaylistUser(idPlayList,idSong).enqueue(object : Callback<DataPlayListUser?> {
            override fun onResponse( call: Call<DataPlayListUser?>, response: Response<DataPlayListUser?>) {
                val dataPlayListUser = response.body()
                if (dataPlayListUser != null) {
                    if (!dataPlayListUser.error) {
                        MyLib.showToast(requireContext(),dataPlayListUser.message)
                    }
                    else {
                        MyLib.showToast(requireContext(),dataPlayListUser.message)
                    }
                }
            }

            override fun onFailure(call: Call<DataPlayListUser?>, t: Throwable) {
                MyLib.showToast(requireContext(),"Call Api Error")
            }
        })
    }
    //endregion





}


