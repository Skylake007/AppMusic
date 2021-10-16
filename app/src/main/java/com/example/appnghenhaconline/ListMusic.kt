package com.example.appnghenhaconline

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.models.song.DataSong
import com.example.appnghenhaconline.models.song.Song
import kotlinx.android.synthetic.main.activity_list_music.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ListMusic : AppCompatActivity() {

    private lateinit var songAdapter: SongAdapter
    private lateinit var songs: ArrayList<Song>

    lateinit var mediaPlayer : MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_music)

        init()
        callApiShowListView()



    }

    private fun callApiShowListView() {
        ApiService.apiService.getListSong().enqueue(object : Callback<DataSong?> {
            override fun onResponse(call: Call<DataSong?>, response: Response<DataSong?>) {
                val dataSong = response.body()
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

    private fun init(){
        songs = ArrayList()
        songAdapter = SongAdapter(this,R.layout.listview_row_song,songs)
        lvSong.adapter = songAdapter
    }

    fun play(v : View) {
        mediaPlayer.start()
    }

    fun pause(v : View) {
        if(mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    private fun playsong(url : String) {
        mediaPlayer = MediaPlayer()
        try {
            mediaPlayer.setDataSource(url)
            mediaPlayer.setOnPreparedListener(object : MediaPlayer.OnPreparedListener {
                override fun onPrepared(mp: MediaPlayer?) {
                    mp?.start()
                }
            })
            mediaPlayer.prepare()
        }catch (e: IOException){
            e.printStackTrace()
        }
    }
}