package com.example.appnghenhaconline.fragment.Library.Playlist

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.adapter.SongAdapter
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.dataLocalManager.Service.MyService
import com.example.appnghenhaconline.models.playlist.Playlist
import com.example.appnghenhaconline.models.song.DataSong
import com.example.appnghenhaconline.models.song.Song
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddPlaylistFragment: Fragment() {
    internal lateinit var view: View
    lateinit var btnBack: ImageView
    lateinit var tvNamePlaylist: TextView
    lateinit var addPlaylistNav: LinearLayout
    lateinit var mPlaylistInfo: Playlist
    lateinit var mediaPlayer : MediaPlayer
    lateinit var listsong : ArrayList<Song>
    lateinit var songAdapter: SongAdapter
    lateinit var rcvSong: RecyclerView
    lateinit var idPlayList : String

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fm_add_playlist_fragment, container, false)

        init()
        return view
    }

    private fun init(){
        btnBack = view.findViewById(R.id.btnBack)
        tvNamePlaylist = view.findViewById(R.id.tvNamePlaylist)
        addPlaylistNav = view.findViewById(R.id.addPlaylistNav)
        initPlaylistInfo()

        event()
    }

    private fun event() {
        btnBack.setOnClickListener {
            val fragmentLayout = LibraryPlaylistFragment()
            MyLib.changeFragment(requireActivity(), fragmentLayout)
        }

        addPlaylistNav.setOnClickListener {
            val bundleReceive = Bundle()
            val fragmentLayout = SearchToAddPlaylistFragment()
            bundleReceive.putSerializable("__object_my_playlist", mPlaylistInfo)
            fragmentLayout.arguments = bundleReceive

            MyLib.changeFragment(requireActivity(), fragmentLayout)
        }

        tvNamePlaylist.setOnClickListener {
            openDialogEditNamePlaylist(Gravity.CENTER)
        }
    }

    //===========================================================
    //region INIT ADAPTER

    private fun initPlaylistInfo(){
        val bundle = requireArguments()
        if (bundle != null){
            if (bundle.getString("_name_playlist") == null){
                val playlistInfo = bundle.get("object_my_playlist")
                mPlaylistInfo = playlistInfo as Playlist
                tvNamePlaylist.text = mPlaylistInfo.playlistname
                idPlayList = mPlaylistInfo.id
                initSongList()
            }else{
                tvNamePlaylist.text = bundle.getString("_name_playlist")
            }
        }
    }

    private fun initSongList(){
        //khởi tạo danh sách bài hát
        listsong = ArrayList()
        songAdapter = SongAdapter(requireContext(),listsong)

        rcvSong = view.findViewById(R.id.rcvMyPlaylistSong)
        rcvSong.setHasFixedSize(true)
        rcvSong.layoutManager = LinearLayoutManager(requireContext(),
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
//                openDialogAddPlaylist(Gravity.CENTER)
            }
        })

        callApiShowListSongByID(listsong,songAdapter,idPlayList)
        mediaPlayer = MediaPlayer()
    }
    //endregion
    //===========================================================
    //region ANOTHER FUNCTION

    //    hàm hiện dialog
    private fun openDialogEditNamePlaylist(gravity: Int){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dlg_edit_name_playlist_dialog)
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

        val edtNamePlaylist: TextInputEditText = dialog.findViewById(R.id.edtNamePlaylist)
        val btnExit: Button = dialog.findViewById(R.id.btnExit)
        val btnAccept: Button = dialog.findViewById(R.id.btnAccept)

        edtNamePlaylist.setText(tvNamePlaylist.text)

        btnExit.setOnClickListener {
            dialog.dismiss()
        }

        btnAccept.setOnClickListener {
            tvNamePlaylist.text = edtNamePlaylist.text
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
    //endregion
}