package com.example.appnghenhaconline.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.adapter.CategoryAdapter
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.adapter.PlaylistSLAdapter
import com.example.appnghenhaconline.adapter.PlaylistSMAdapter
import com.example.appnghenhaconline.models.playlist.Category
import com.example.appnghenhaconline.models.playlist.DataPlayList
import com.example.appnghenhaconline.models.playlist.Playlist
import com.example.appnghenhaconline.models.user.DataUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlayNowFragment : Fragment() {
    internal lateinit var view: View
    lateinit var rcvCategory: RecyclerView
    lateinit var listPlaylist : ArrayList<Playlist>
    lateinit var playlistAdapterSL : PlaylistSLAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fm_play_now_fragment, container, false)


       initCategoryList()

        return view
    }

    private fun initCategoryList(){
        listPlaylist = ArrayList()
        playlistAdapterSL = PlaylistSLAdapter(view.context,listPlaylist)
        rcvCategory = view.findViewById(R.id.rcvCategory1)
        rcvCategory.setHasFixedSize(true)
        rcvCategory.layoutManager = LinearLayoutManager(view.context,
            LinearLayoutManager.HORIZONTAL,false)
        rcvCategory.adapter = playlistAdapterSL
        callApiPlayList(listPlaylist,playlistAdapterSL,"EDM")

//        var playlistAdapterSM1 = PlaylistSMAdapter(view.context,getListPlaylist())
//        rcvCategory = view.findViewById(R.id.rcvCategory2)
//        rcvCategory.setHasFixedSize(true)
//        rcvCategory.layoutManager = LinearLayoutManager(view.context,
//            LinearLayoutManager.HORIZONTAL,false)
//        rcvCategory.adapter = playlistAdapterSM1
//
//        var playlistAdapterSM2 = PlaylistSMAdapter(view.context,getListPlaylist())
//        rcvCategory = view.findViewById(R.id.rcvCategory3)
//        rcvCategory.setHasFixedSize(true)
//        rcvCategory.layoutManager = LinearLayoutManager(view.context,
//            LinearLayoutManager.HORIZONTAL,false)
//        rcvCategory.adapter = playlistAdapterSM2
//
//        var playlistAdapterSM3 = PlaylistSMAdapter(view.context,getListPlaylist())
//        rcvCategory = view.findViewById(R.id.rcvCategory4)
//        rcvCategory.setHasFixedSize(true)
//        rcvCategory.layoutManager = LinearLayoutManager(view.context,
//            LinearLayoutManager.HORIZONTAL,false)
//        rcvCategory.adapter = playlistAdapterSM3
    }

//    private fun getListPlaylist(): ArrayList<Playlist>{
//        var listPlaylist: ArrayList<Playlist> = ArrayList()
////        listPlaylist.add(Playlist("abc","Phan Mạnh Quỳnh",R.drawable.cv_img1,
////            Category("abc","Nhạc giực")))
////        listPlaylist.add(Playlist("abc","Phan Mạnh Quỳnh",R.drawable.cv_img1,
////            Category("abc","Nhạc giực")))
////        listPlaylist.add(Playlist("abc","Phan Mạnh Quỳnh",R.drawable.cv_img1,
////            Category("abc","Nhạc giực")))
////        listPlaylist.add(Playlist("abc","Phan Mạnh Quỳnh",R.drawable.cv_img1,
////            Category("abc","Nhạc giực")))
//        return listPlaylist
//    }

    private fun callApiPlayList(list : ArrayList<Playlist>, adapter : PlaylistSLAdapter, categoryName : String) {
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