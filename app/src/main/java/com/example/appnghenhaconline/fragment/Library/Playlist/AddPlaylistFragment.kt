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
import com.example.appnghenhaconline.adapter.SongRemoveAdapter
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.dataLocalManager.Service.MyService
import com.example.appnghenhaconline.models.playlist.DataPlayList
import com.example.appnghenhaconline.models.playlist.DataPlayListUser
import com.example.appnghenhaconline.models.playlist.PlayListUser
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
//    lateinit var mPlaylistInfo: PlayListUser
    var mediaPlayer : MediaPlayer = MediaPlayer()
    lateinit var listsong : ArrayList<Song>
    lateinit var songRemoveAdapter: SongRemoveAdapter
    lateinit var rcvSong: RecyclerView
    lateinit var idPlayList : String
    lateinit var btnDeletePlayListUser : ImageView

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
        btnDeletePlayListUser = view.findViewById(R.id.btnDeletePlaylistUser)
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
            bundleReceive.putSerializable("id_playlist", idPlayList)
            fragmentLayout.arguments = bundleReceive

            MyLib.changeFragment(requireActivity(), fragmentLayout)
        }

        tvNamePlaylist.setOnClickListener {
            openDialogEditNamePlaylist(Gravity.CENTER)
        }

        btnDeletePlayListUser.setOnClickListener{
            callApiDeletePlaylistUser(idPlayList)
        }
    }

    //===========================================================
    //region INIT ADAPTER

    private fun initPlaylistInfo(){
        val bundle = requireArguments()
        if (bundle != null){
            idPlayList = bundle.getString("id_playlist")!!
            initSongList()
            callApiShowSongFromPlaylistUser(listsong,songRemoveAdapter,idPlayList)
        }
    }

    private fun initSongList(){
        //khởi tạo danh sách bài hát
        listsong = ArrayList()
        songRemoveAdapter = SongRemoveAdapter(requireContext(),listsong)

        rcvSong = view.findViewById(R.id.rcvMyPlaylistSong)
        rcvSong.setHasFixedSize(true)
        rcvSong.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        rcvSong.adapter = songRemoveAdapter

        //Sự kiện onItemClick
        songRemoveAdapter.setOnItemClickListener(object : SongRemoveAdapter.IonItemClickListener{
            override fun onItemClick(position: Int) {
                if (mediaPlayer.isPlaying){
                    mediaPlayer.stop()
                    mediaPlayer.release()
                    clickStartService(listsong, position)
                }else{
                    clickStartService(listsong, position)
                }
            }
            override fun onItemSelected(position: Int) {
                callApiDeleteSongFromPlaylistUser(idPlayList,listsong[position].id)
            }
        })
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
            var namePlaylist = edtNamePlaylist.text.toString().trim()
            if ( namePlaylist == "") {
                MyLib.showToast(requireContext(),"Vui Lòng không để trống Tên playList")
            }
            else {
                callApiUpdateNameFromPlayListUser(idPlayList,namePlaylist)
                dialog.dismiss()
            }
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

    //Show list song in playlist
    private fun callApiShowSongFromPlaylistUser(songs : ArrayList<Song>,songAdapter : SongRemoveAdapter, idPlaylistUser : String ) {
        ApiService.apiService.showSongFromPlaylistUser(idPlaylistUser).enqueue(object : Callback<DataPlayListUser?> {
            override fun onResponse( call: Call<DataPlayListUser?>, response: Response<DataPlayListUser?>) {
                val dataPlayListUser = response.body()
                if (dataPlayListUser != null) {
                    if (!dataPlayListUser.error) {
                        var playListUser = dataPlayListUser.playlistUser
                        var listSong : ArrayList<Song> = playListUser.listSong
                        MyLib.showLog(playListUser.toString())
                        tvNamePlaylist.text = playListUser.playlistName
                        songs.addAll(listSong)
                        songAdapter.notifyDataSetChanged()
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

    // update name playlist
    private fun callApiUpdateNameFromPlayListUser(idPlaylistUser : String, namePlaylistUser : String ) {
        ApiService.apiService.updateNameFromPlayListUser(idPlaylistUser,namePlaylistUser).enqueue(object : Callback<DataPlayListUser?> {
            override fun onResponse( call: Call<DataPlayListUser?>, response: Response<DataPlayListUser?>) {
                val dataPlayListUser = response.body()
                if (dataPlayListUser != null) {
                    if (!dataPlayListUser.error) {
                        MyLib.showToast(requireContext(),dataPlayListUser.message)
                        val fragmentLayout = AddPlaylistFragment()

                        val bundle = Bundle()
                        bundle.putSerializable("id_playlist",idPlayList)
                        fragmentLayout.arguments = bundle

                        MyLib.changeFragment(requireActivity(), fragmentLayout)
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

    // Delete playlist
    private fun callApiDeletePlaylistUser(idPlaylistUser: String) {
        ApiService.apiService.deletePlaylistUser(idPlaylistUser).enqueue(object : Callback<DataPlayListUser?> {
            override fun onResponse(call: Call<DataPlayListUser?>, response: Response<DataPlayListUser?>) {
                val dataPlayListUser = response.body()
                if (dataPlayListUser != null) {
                    if (!dataPlayListUser.error) {
                        MyLib.showToast(requireContext(),dataPlayListUser.message)
                        val fragmentLayout = LibraryPlaylistFragment()

                        val bundle = Bundle()

                        fragmentLayout.arguments = bundle

                        MyLib.changeFragment(requireActivity(), fragmentLayout)
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

    // delete song from playlist
    private fun callApiDeleteSongFromPlaylistUser(idPlaylistUser: String, idSong : String) {
        ApiService.apiService.deleteSongFromPlaylistUser(idPlaylistUser,idSong).enqueue(object : Callback<DataPlayListUser?> {
            override fun onResponse(call: Call<DataPlayListUser?>, response: Response<DataPlayListUser?>) {
                val dataPlayListUser = response.body()
                if (dataPlayListUser != null) {
                    if (!dataPlayListUser.error) {
                        MyLib.showToast(requireContext(),dataPlayListUser.message)
                        val fragmentLayout = AddPlaylistFragment()

                        val bundle = Bundle()
                        bundle.putSerializable("id_playlist",idPlayList)
                        fragmentLayout.arguments = bundle

                        MyLib.changeFragment(requireActivity(), fragmentLayout)
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