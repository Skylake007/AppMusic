package com.example.appnghenhaconline.fragment

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
import com.squareup.picasso.Picasso
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class AlbumFragment: Fragment() {

    internal lateinit var view: View
    lateinit var rcvSong: RecyclerView
    lateinit var tittleAlbum : TextView
    lateinit var imgAlbum : ImageView
    lateinit var  listsong : ArrayList<Song>
    lateinit var  idPlayList : String
    lateinit var mediaPlayer : MediaPlayer
    lateinit var songNAdapter: SongNAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fm_album_music_fragment, container, false)

        tittleAlbum = view.findViewById(R.id.tittleAlbumMusic)
        imgAlbum = view.findViewById(R.id.imgAlbumMusic)

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
        songNAdapter.setOnItemClickListener(object : SongNAdapter.IonItemClickListener{
            override fun onItemClick(position: Int) {
                if (mediaPlayer.isPlaying){
                    mediaPlayer.stop()
                    mediaPlayer.release()
                    playSong(listsong[position].link)
                    MyLib.showLog("AlbumFragment: "+ listsong[position].link)
                } else {
                    playSong(listsong[position].link)
                    MyLib.showLog("AlbumFragment: "+ listsong[position].link)
                }
            }

        })
        callApiShowListSongByID(listsong,songNAdapter,idPlayList)
        mediaPlayer = MediaPlayer()
    }

    private fun initAlbum(){  // khi nhấn vào item
        val bundleReceive : Bundle = requireArguments()
        if (bundleReceive!= null){
            val playlist : Playlist = bundleReceive["object_song"] as Playlist
            if (playlist != null){
                tittleAlbum.text = playlist.playlistname
                idPlayList = playlist.id // get id of playlist
                Picasso.get().load(playlist.image).into(imgAlbum)
            }
        }
    }

    private fun callApiShowListSongByID(songs : ArrayList<Song>, songAdapter : SongNAdapter, id : String ) {
        
        ApiService.apiService.getListSongByID(id).enqueue(object : Callback<DataSong?> {
            override fun onResponse(call: Call<DataSong?>, response: Response<DataSong?>) {
                val dataSong = response.body()
                MyLib.showLog(dataSong.toString())
                if(dataSong!=null){
                    if(!dataSong!!.error){
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

    fun play(v : View) { // button play music
        mediaPlayer.start()
    }

    fun pause(v : View) { // buttion pause
        if(mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    private fun playSong(url : String) { //funtion play music
        try {
            mediaPlayer = MediaPlayer()
            mediaPlayer.setDataSource(url)
            mediaPlayer.setOnPreparedListener { mp -> mp?.start() }
            mediaPlayer.prepare()
        }catch (e: IOException){
            e.printStackTrace()
        }
    }

}


