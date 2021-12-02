package com.example.appnghenhaconline.fragment.SingerInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.adapter.AlbumAdapter
import com.example.appnghenhaconline.adapter.SongAdapter
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.models.album.Album
import com.example.appnghenhaconline.models.album.DataAlbum
import com.example.appnghenhaconline.models.song.DataSong
import com.example.appnghenhaconline.models.song.Song
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AlbumOfSingerFragment(idSinger: String): Fragment() {

    internal lateinit var view: View
    private lateinit var rcvAlbum: RecyclerView
    private lateinit var listAlbum : ArrayList<Album>
    private lateinit var albumAdapter: AlbumAdapter
    private var idSinger: String = idSinger

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.tab_album_of_singer, container, false)
        MyLib.showLog("AlbumOfSinger : $idSinger")
        init()

        return view
    }

    private fun init(){
//        initIdSinger()
        initAlbum()
    }

    private fun initIdSinger() {
        val bundleReceive : Bundle = requireArguments()
        idSinger = bundleReceive.getString("id_singer").toString()
    }

    private fun initAlbum(){
        listAlbum = ArrayList()
        albumAdapter = AlbumAdapter(view.context,listAlbum)
        rcvAlbum = view.findViewById(R.id.rcvAlbum)

        rcvAlbum.layoutManager = GridLayoutManager(view.context,2)
        rcvAlbum.setHasFixedSize(true)
        rcvAlbum.adapter = albumAdapter

        callApiShowListAlbumByIdSinger(listAlbum,albumAdapter,idSinger)
    }

    private fun callApiAlbum(list : ArrayList<Album>, adapter : AlbumAdapter) {
        ApiService.apiService.getAllAlbum().enqueue(object : Callback<DataAlbum?> {
            override fun onResponse(call: Call<DataAlbum?>, response: Response<DataAlbum?>) {
                var dataAlbum = response.body()
                if(dataAlbum != null) {
                    if (!dataAlbum.error) {
                        list.addAll(dataAlbum.albums)
                        adapter.notifyDataSetChanged()
                    }
                    else {
                        MyLib.showLog("PlayNowFragment.kt: " + dataAlbum.message)
                    }
                }
            }

            override fun onFailure(call: Call<DataAlbum?>, t: Throwable) {
                MyLib.showToast(requireContext(),"Call Api Error")
            }

        })
    }

    private fun callApiShowListAlbumByIdSinger(album : ArrayList<Album>,albumAdapter : AlbumAdapter, idSinger : String ) {
        ApiService.apiService.getListAlbumBySingerId(idSinger).enqueue(object : Callback<DataAlbum?> {
            override fun onResponse(call: Call<DataAlbum?>, response: Response<DataAlbum?>) {
                val dataAlbum = response.body()
                MyLib.showLog(dataAlbum.toString())
                if(dataAlbum!=null){
                    if(!dataAlbum.error){
                        val listAlbum: ArrayList<Album> = dataAlbum.albums

                        album.addAll(listAlbum)

                        albumAdapter.notifyDataSetChanged()
                    }else MyLib.showLog(dataAlbum.message)
                }
            }

            override fun onFailure(call: Call<DataAlbum?>, t: Throwable) {
                MyLib.showLog(t.toString())
            }
        })
    }
}