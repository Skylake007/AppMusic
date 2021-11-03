package com.example.appnghenhaconline.fragment

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.adapter.SongNAdapter
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.models.playlist.Playlist
import com.example.appnghenhaconline.models.song.DataSong
import com.example.appnghenhaconline.models.song.Song
import com.example.appnghenhaconline.service.MyService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class AlbumFragment: Fragment() {

    internal lateinit var view: View
    lateinit var rcvSong: RecyclerView
    lateinit var tittleAlbum : TextView
    lateinit var imgAlbum : ImageView
    lateinit var listsong : ArrayList<Song>
    lateinit var idPlayList : String
    lateinit var mediaPlayer : MediaPlayer
    lateinit var songNAdapter: SongNAdapter
    lateinit var btnNext: ImageView
    lateinit var btnPre: ImageView

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fm_album_music_fragment, container, false)

        tittleAlbum = view.findViewById(R.id.tittleAlbumMusic)
        imgAlbum = view.findViewById(R.id.imgAlbumMusic)
//        btnNext = view.findViewById(R.id.btnNext)
//        btnPre = view.findViewById(R.id.btnPre)

        initAlbum()
        initSongList()

        return view
    }

    private fun initSongList(){
        listsong = ArrayList()
        songNAdapter = SongNAdapter(view.context,listsong)

        rcvSong = view.findViewById(R.id.rcvSong)
        rcvSong.setHasFixedSize(true)
        rcvSong.layoutManager = LinearLayoutManager(view.context,
                                LinearLayoutManager.VERTICAL,false)
        rcvSong.adapter = songNAdapter
        //Sự kiện onItemClick
        songNAdapter.setOnItemClickListener(object : SongNAdapter.IonItemClickListener{
            override fun onItemClick(position: Int) {
                if (mediaPlayer.isPlaying){
                    mediaPlayer.stop()
                    clickStartService(listsong[position])
                }else{
                    clickStartService(listsong[position])
                }
                MyLib.showLog("AlbumFragment: "+ listsong[position].link)
            }
        })

        callApiShowListSongByID(listsong,songNAdapter,idPlayList)
        mediaPlayer = MediaPlayer()
    }

    private fun initAlbum(){  // khi nhấn vào item
        val bundleReceive : Bundle = requireArguments()
        val playlist : Playlist = bundleReceive["object_song"] as Playlist

        tittleAlbum.text = playlist.playlistname
        idPlayList = playlist.id // get id of playlist
        Picasso.get().load(playlist.image).resize(800,800).into(imgAlbum)
    }

    private fun callApiShowListSongByID(songs : ArrayList<Song>, songAdapter : SongNAdapter, id : String ) {
        
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

    private fun clickStartService(song: Song) {
        val intent = Intent(requireContext(), MyService::class.java)

        val bundle = Bundle()
        bundle.putSerializable("item_song", song)
        intent.putExtras(bundle)

        activity?.startService(intent)
        MyLib.showLog("AlbumFragment: Running service")
    }
}


