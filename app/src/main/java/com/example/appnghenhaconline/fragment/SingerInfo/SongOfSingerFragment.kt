package com.example.appnghenhaconline.fragment.SingerInfo

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.adapter.SongAdapter
import com.example.appnghenhaconline.dataLocalManager.Service.MyService
import com.example.appnghenhaconline.models.song.Song

class SongOfSingerFragment(idSinger: String): Fragment() {

    internal lateinit var view: View
    private lateinit var songAdapter: SongAdapter
    private lateinit var rcvSong: RecyclerView
    private lateinit var listSong: ArrayList<Song>
    private var idSinger: String = idSinger

    private var mediaPlayer : MediaPlayer = MediaPlayer()

    private lateinit var lavFollow : LottieAnimationView
    private lateinit var btnPlayPlaylist : Button
    private lateinit var btnShufflePlaylist : Button
    private var isFollow: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.tab_song_of_singer, container, false)
        init()
        MyLib.showLog("SongOfSinger : $idSinger")
        return view
    }

    private fun init(){

        initSongOfSinger()
    }


    private fun initSongOfSinger(){
        listSong = ArrayList()
        songAdapter = SongAdapter(view.context,listSong)

        rcvSong = view.findViewById(R.id.rcvSong)
        rcvSong.setHasFixedSize(true)
        rcvSong.layoutManager = LinearLayoutManager(view.context,
            LinearLayoutManager.VERTICAL,false)
        rcvSong.adapter = songAdapter

        songAdapter.setOnItemClickListener(object: SongAdapter.IonItemClickListener{
            override fun onItemClick(position: Int) {
//                if (mediaPlayer.isPlaying){
//                    mediaPlayer.stop()
//                    clickStartService(listSong, position)
//                }else{
//                    clickStartService(listSong, position)
//                }
            }

            override fun onItemSelected(position: Int) {
//                openDialogAddPlaylist(Gravity.CENTER, listSong[position].id)
            }
        })
    }

    private fun sendActionToService(action: Int){
        val intent = Intent(requireContext(), MyService::class.java)
        intent.putExtra("action_music_service", action)
        requireActivity().startService(intent)
    }

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

//        initListPlaylist(rcvPlaylistSelected, idSong)

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
}