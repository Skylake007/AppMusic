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
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.adapter.PlaylistSLAdapter
import com.example.appnghenhaconline.adapter.PlaylistSMAdapter
import com.example.appnghenhaconline.models.playlist.DataPlayList
import com.example.appnghenhaconline.models.playlist.Playlist
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlayNowFragment : Fragment() {
    internal lateinit var view: View
    lateinit var rcvCategory: RecyclerView
    lateinit var listPlaylist : ArrayList<Playlist>
    lateinit var playlistAdapterSL : PlaylistSLAdapter
    lateinit var playlistAdapterSM : PlaylistSMAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fm_play_now_fragment, container, false)
       initCategoryList()

        return view
    }

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

        //Playlist_3
        listPlaylist = ArrayList()
        playlistAdapterSM = PlaylistSMAdapter(view.context,listPlaylist)
        rcvCategory = view.findViewById(R.id.rcvCategory3)

        createCategorySM(rcvCategory, playlistAdapterSM)
        callApiPlayListSM(listPlaylist,playlistAdapterSM)

        //Playlist_4
        listPlaylist = ArrayList()
        playlistAdapterSM = PlaylistSMAdapter(view.context,listPlaylist)
        rcvCategory = view.findViewById(R.id.rcvCategory4)

        createCategorySM(rcvCategory, playlistAdapterSM)
        callApiPlayListSM(listPlaylist,playlistAdapterSM)
    }

    private fun createCategorySL(rcv: RecyclerView, adapter: PlaylistSLAdapter){
        rcv.layoutManager = LinearLayoutManager(view.context,
                            LinearLayoutManager.HORIZONTAL, false)
        rcv.setHasFixedSize(true)
        rcv.adapter = adapter
    }

    private fun createCategorySM(rcv: RecyclerView, adapter: PlaylistSMAdapter){
        rcv.layoutManager = LinearLayoutManager(view.context,
            LinearLayoutManager.HORIZONTAL, false)
        rcv.setHasFixedSize(true)
        rcv.adapter = adapter
    }

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
}