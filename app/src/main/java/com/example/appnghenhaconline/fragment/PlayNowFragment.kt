package com.example.appnghenhaconline.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.adapter.AlbumAdapter
import com.example.appnghenhaconline.adapter.FollowAlbumAdapter
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.adapter.PlaylistSLAdapter
import com.example.appnghenhaconline.adapter.PlaylistSMAdapter
import com.example.appnghenhaconline.models.album.Album
import com.example.appnghenhaconline.models.album.DataAlbum
import com.example.appnghenhaconline.models.playlist.DataPlayList
import com.example.appnghenhaconline.models.playlist.Playlist
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlayNowFragment : Fragment() {
    internal lateinit var view: View
    private lateinit var rcvCategory: RecyclerView
    private lateinit var listPlaylist : ArrayList<Playlist>
    private lateinit var listAlbum : ArrayList<Album>
    private lateinit var playlistAdapterSL : PlaylistSLAdapter
    private lateinit var playlistAdapterSM : PlaylistSMAdapter
    private lateinit var albumAdapter: AlbumAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fm_play_now_fragment, container, false)
        initCategoryList()
        return view
    }

    //region INIT ADAPTER

    private fun initCategoryList(){
        //Khởi tạo danh sách playlist
        //Playlist_1
        listPlaylist = ArrayList()
        playlistAdapterSL = PlaylistSLAdapter(view.context,listPlaylist)
        rcvCategory = view.findViewById(R.id.rcvCategory1)

        createCategorySL(rcvCategory, playlistAdapterSL)
        callApiPlayList(listPlaylist,playlistAdapterSL)

        //Playlist_2
        listPlaylist = ArrayList()
        playlistAdapterSM = PlaylistSMAdapter(view.context,listPlaylist)
        rcvCategory = view.findViewById(R.id.rcvCategory2)

        createCategorySM(rcvCategory, playlistAdapterSM)
        callApiPlayListSM(listPlaylist,playlistAdapterSM)

        //Album_1
        listAlbum = ArrayList()
        albumAdapter = AlbumAdapter(view.context,listAlbum)
        rcvCategory = view.findViewById(R.id.rcvCategory3)

        createAlbumSM(rcvCategory, albumAdapter)
        callApiAlbum(listAlbum,albumAdapter)

        //Album_2
        listAlbum = ArrayList()
        albumAdapter = AlbumAdapter(view.context,listAlbum)
        rcvCategory = view.findViewById(R.id.rcvCategory4)

        createAlbumSM(rcvCategory, albumAdapter)
        callApiAlbum(listAlbum,albumAdapter)
    }
    //endregion
    //===========================================================
    //region ANOTHER FUNCTION

    // set adapter cho Playlist SL
    private fun createCategorySL(rcv: RecyclerView, adapter: PlaylistSLAdapter){
        rcv.layoutManager = LinearLayoutManager(view.context,
                            LinearLayoutManager.HORIZONTAL, false)
        rcv.setHasFixedSize(true)
        rcv.adapter = adapter
    }

    // set adapter cho Playlist SM
    private fun createCategorySM(rcv: RecyclerView, adapter: PlaylistSMAdapter){
        rcv.layoutManager = LinearLayoutManager(view.context,
            LinearLayoutManager.HORIZONTAL, false)
        rcv.setHasFixedSize(true)
        rcv.adapter = adapter
    }

    // set adapter cho Album
    private fun createAlbumSM(rcv: RecyclerView, adapter: AlbumAdapter){
        rcv.layoutManager = LinearLayoutManager(view.context,
            LinearLayoutManager.HORIZONTAL, false)
        rcv.setHasFixedSize(true)
        rcv.adapter = adapter
    }
    //endregion
    //===========================================================
    //region CALL API

    private fun callApiPlayList(list : ArrayList<Playlist>, adapter : PlaylistSLAdapter) {
        ApiService.apiService.getPlayList().enqueue(object : Callback<DataPlayList?> {
            override fun onResponse(call: Call<DataPlayList?>, response: Response<DataPlayList?>) {
                var dataPlayList = response.body()
                if(dataPlayList != null) {
                    if(!dataPlayList.error) {
                        list.addAll(dataPlayList.listPlayList)
                        adapter.notifyDataSetChanged()
                    }
                    else {
                        MyLib.showLog("PlayNowFragment.kt: " + dataPlayList.message)
                    }
                }
            }

            override fun onFailure(call: Call<DataPlayList?>, t: Throwable) {
                MyLib.showToast(requireContext(),"Call Api Error")
            }
        })
    }

    private fun callApiPlayListSM(list : ArrayList<Playlist>, adapter : PlaylistSMAdapter) {
        ApiService.apiService.getPlayList().enqueue(object : Callback<DataPlayList?> {
            override fun onResponse(call: Call<DataPlayList?>, response: Response<DataPlayList?>) {
                var dataPlayList = response.body()
                if(dataPlayList != null) {
                    if(!dataPlayList.error) {
                        list.addAll(dataPlayList.listPlayList)
                        adapter.notifyDataSetChanged()
                    }
                    else {
                        MyLib.showLog("PlayNowFragment.kt: " + dataPlayList.message)
                    }
                }
            }

            override fun onFailure(call: Call<DataPlayList?>, t: Throwable) {
                MyLib.showToast(requireContext(),"Call Api Error")
            }
        })
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
    //endregion
}