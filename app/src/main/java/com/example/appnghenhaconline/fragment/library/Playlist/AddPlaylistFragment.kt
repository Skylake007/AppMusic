package com.example.appnghenhaconline.fragment.library.Playlist

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
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.activity.PlayMusicActivity
import com.example.appnghenhaconline.adapter.songAdapter.SongRemoveAdapter
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.dataLocalManager.MyDataLocalManager
import com.example.appnghenhaconline.dataLocalManager.Service.MyService
import com.example.appnghenhaconline.fragment.library.Playlist.LibraryPlaylistFragment.Companion.btnAddPlaylist
import com.example.appnghenhaconline.models.playlist.DataPlayListUser
import com.example.appnghenhaconline.models.song.Song
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
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
    lateinit var imgAddPlaylist: ImageView
    lateinit var btnChangeName: ImageView
    private lateinit var btnPlayPlaylist: Button
    private lateinit var btnShufflePlaylist: Button

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fm_add_playlist_fragment, container, false)

        init()
        return view
    }

    override fun onStart() {
        super.onStart()
        initPlaylistInfo()
        event()
    }

    private fun init(){
        btnBack = view.findViewById(R.id.btnBack)
        tvNamePlaylist = view.findViewById(R.id.tvNamePlaylist)
        addPlaylistNav = view.findViewById(R.id.addPlaylistNav)
        btnDeletePlayListUser = view.findViewById(R.id.btnDeletePlaylistUser)
        imgAddPlaylist = view.findViewById(R.id.imgAddPlaylist)
        btnPlayPlaylist = view.findViewById(R.id.btnPlayPlaylist)
        btnShufflePlaylist = view.findViewById(R.id.btnShufflePlaylist)
        btnChangeName = view.findViewById(R.id.btnChangeName)
    }


    private fun event() {
        if(btnAddPlaylist.isVisible){
            btnAddPlaylist.hide()
        }

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

        btnChangeName.setOnClickListener {
            openDialogEditNamePlaylist(Gravity.CENTER)
        }

        btnDeletePlayListUser.setOnClickListener{
            openDialogRemovePlaylist(Gravity.CENTER)
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
        //kh???i t???o danh s??ch b??i h??t
        listsong = ArrayList()
        songRemoveAdapter = SongRemoveAdapter(requireContext(),listsong)

        rcvSong = view.findViewById(R.id.rcvMyPlaylistSong)
        rcvSong.setHasFixedSize(true)
        rcvSong.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        rcvSong.adapter = songRemoveAdapter

        //S??? ki???n onItemClick
        songRemoveAdapter.setOnItemClickListener(object : SongRemoveAdapter.IonItemClickListener{
            override fun onClickItem(position: Int) {
                clickStartService(listsong, position)
            }

            override fun onRemoveItem(position: Int) {
                openDialogRemoveSong(Gravity.CENTER, position)
            }
        })
    }

    private fun initEventPlay(songs : ArrayList<Song>){
        if (songs.isNotEmpty()){
            btnPlayPlaylist.setOnClickListener {
                PlayMusicActivity.isShuffle = false
                clickStartService(songs,position = 0)
                MyDataLocalManager.setIsShuffle(PlayMusicActivity.isShuffle)
            }
            btnShufflePlaylist.setOnClickListener {
                PlayMusicActivity.isShuffle = true
                clickStartService(songs,position = 0)
                sendActionToService(MyService.ACTION_NEXT)
                MyDataLocalManager.setIsShuffle(PlayMusicActivity.isShuffle)
            }
        }else{
            btnPlayPlaylist.setOnClickListener {
                MyLib.showToast(requireContext(),"Kh??ng c?? b??i h??t n??o!")
            }
            btnShufflePlaylist.setOnClickListener {
                MyLib.showToast(requireContext(),"Kh??ng c?? b??i h??t n??o!")
            }
        }
    }

    private fun sendActionToService(action: Int){
        val intent = Intent(requireContext(), MyService::class.java)
        intent.putExtra("action_music_service", action)
        requireActivity().startService(intent)
    }
    //endregion
    //===========================================================
    //region ANOTHER FUNCTION

    //h??m hi???n dialog s???a t??n playlist
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
                MyLib.showToast(requireContext(),"Vui L??ng kh??ng ????? tr???ng T??n playList")
            }
            else {
                callApiUpdateNameFromPlayListUser(idPlayList,namePlaylist)
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    //h??m hi???n dialog x??a b??i h??t
    private fun openDialogRemoveSong(gravity: Int, position: Int){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dlg_remove_song_dialog)
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
        val btnAccept: Button = dialog.findViewById(R.id.btnAccept)

        btnExit.setOnClickListener {
            dialog.dismiss()
        }

        btnAccept.setOnClickListener {
            callApiDeleteSongFromPlaylistUser(idPlayList,listsong[position].id)
            dialog.dismiss()
        }
        dialog.show()
    }

    //h??m hi???n dialog x??a b??i h??t
    private fun openDialogRemovePlaylist(gravity: Int){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dlg_remove_playlist_dialog)
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
        val btnAccept: Button = dialog.findViewById(R.id.btnAccept)

        btnExit.setOnClickListener {
            dialog.dismiss()
        }

        btnAccept.setOnClickListener {
            callApiDeletePlaylistUser(idPlayList)
            dialog.dismiss()
        }
        dialog.show()
    }


    //call service x??? l?? x??? ki???n ph??t nh???c
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
    private fun callApiShowSongFromPlaylistUser(songs : ArrayList<Song>, songAdapter : SongRemoveAdapter, idPlaylistUser : String ) {
        ApiService.apiService.showSongFromPlaylistUser(idPlaylistUser).enqueue(object : Callback<DataPlayListUser?> {
            override fun onResponse( call: Call<DataPlayListUser?>, response: Response<DataPlayListUser?>) {
                val dataPlayListUser = response.body()
                if (dataPlayListUser != null) {
                    if (!dataPlayListUser.error) {
                        var playListUser = dataPlayListUser.playlistUser
                        var listSong : ArrayList<Song> = playListUser.listSong
                        MyLib.showLog(playListUser.toString())
                        tvNamePlaylist.text = playListUser.playlistName
                        if (listSong.size != 0){
                            Picasso.get().load(listSong[0].image).into(imgAddPlaylist)
                        }
//                        else{
//                            Picasso.get().load(R.drawable.ic_music_note_grey).into(imgAddPlaylist)
//                        }

                        songs.addAll(listSong)
                        initEventPlay(songs)
                        songAdapter.notifyDataSetChanged()
                    }
                    else {
//                        MyLib.showToast(requireContext(),dataPlayListUser.message)
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