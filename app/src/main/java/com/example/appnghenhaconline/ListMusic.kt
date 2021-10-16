package com.example.appnghenhaconline

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.models.song.DataSong
import com.example.appnghenhaconline.models.song.Song
import kotlinx.android.synthetic.main.activity_list_music.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ListMusic : AppCompatActivity() {

    private lateinit var songAdapter: SongAdapter
    private lateinit var songs: ArrayList<Song>

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
}