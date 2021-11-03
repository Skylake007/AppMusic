package com.example.appnghenhaconline

import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.example.appnghenhaconline.adapter.SongNAdapter
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.models.song.DataSong
import com.example.appnghenhaconline.models.song.Song
import kotlinx.android.synthetic.main.activity_list_music.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

class ListMusic : AppCompatActivity() {

    private lateinit var songAdapter: SongNAdapter
    private lateinit var songs: ArrayList<Song>

    private lateinit var mediaPlayer : MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_music)

        init()
        callApiShowListView()

        // sĩ viết
        lvSong.onItemClickListener =
            AdapterView.OnItemClickListener { parent, view, position, id -> //playsong(arraylistSongUrl.get(position))
                //Toast.makeText(this@MainActivity,arraylistSongName.get(position),Toast.LENGTH_SHORT).show()
                !lvSong.isFocusable
                if (mediaPlayer.isPlaying){
                    mediaPlayer.stop()
                    playsong(songs.get(position).link)
                    Toast.makeText(this@ListMusic,songs.get(position).title,Toast.LENGTH_SHORT).show()
                } else {
                    playsong(songs.get(position).link)
                    Toast.makeText(this@ListMusic,songs.get(position).title, Toast.LENGTH_SHORT).show()
                }
            }

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
        songAdapter = SongNAdapter(this, songs)
//        lvSong.adapter = songAdapter
        mediaPlayer = MediaPlayer() // create media sĩ viết
    }

    // phần sĩ viết dưới này..............
    fun play(v : View) { // button play music
        mediaPlayer.start()
    }

    fun pause(v : View) { // buttion pause
        if(mediaPlayer.isPlaying) {
            mediaPlayer.pause()
        }
    }

    private fun playsong(url : String) { //funtion play music
        try {
            mediaPlayer = MediaPlayer()
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