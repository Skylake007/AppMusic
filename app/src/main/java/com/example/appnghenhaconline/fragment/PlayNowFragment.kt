package com.example.appnghenhaconline.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.adapter.CategoryAdapter
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.adapter.PlaylistSLAdapter
import com.example.appnghenhaconline.adapter.PlaylistSMAdapter
import com.example.appnghenhaconline.models.playlist.Category
import com.example.appnghenhaconline.models.playlist.Playlist

class PlayNowFragment : Fragment() {
    internal lateinit var view: View
    lateinit var rcvCategory: RecyclerView

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fm_play_now_fragment, container, false)


        initCategoryList()
        return view
    }

    private fun initCategoryList(){
        var playlistAdapterSL = PlaylistSLAdapter(view.context,getListPlaylist())
        rcvCategory = view.findViewById(R.id.rcvCategory1)
        rcvCategory.setHasFixedSize(true)
        rcvCategory.layoutManager = LinearLayoutManager(view.context,
            LinearLayoutManager.HORIZONTAL,false)
        rcvCategory.adapter = playlistAdapterSL

        var playlistAdapterSM1 = PlaylistSMAdapter(view.context,getListPlaylist())
        rcvCategory = view.findViewById(R.id.rcvCategory2)
        rcvCategory.setHasFixedSize(true)
        rcvCategory.layoutManager = LinearLayoutManager(view.context,
            LinearLayoutManager.HORIZONTAL,false)
        rcvCategory.adapter = playlistAdapterSM1

        var playlistAdapterSM2 = PlaylistSMAdapter(view.context,getListPlaylist())
        rcvCategory = view.findViewById(R.id.rcvCategory3)
        rcvCategory.setHasFixedSize(true)
        rcvCategory.layoutManager = LinearLayoutManager(view.context,
            LinearLayoutManager.HORIZONTAL,false)
        rcvCategory.adapter = playlistAdapterSM2

        var playlistAdapterSM3 = PlaylistSMAdapter(view.context,getListPlaylist())
        rcvCategory = view.findViewById(R.id.rcvCategory4)
        rcvCategory.setHasFixedSize(true)
        rcvCategory.layoutManager = LinearLayoutManager(view.context,
            LinearLayoutManager.HORIZONTAL,false)
        rcvCategory.adapter = playlistAdapterSM3
    }

    private fun getListPlaylist(): ArrayList<Playlist>{
        var listPlaylist: ArrayList<Playlist> = ArrayList()
        listPlaylist.add(Playlist("abc","Phan Mạnh Quỳnh",R.drawable.cv_img1,
            Category("abc","Nhạc giực")))
        listPlaylist.add(Playlist("abc","Phan Mạnh Quỳnh",R.drawable.cv_img1,
            Category("abc","Nhạc giực")))
        listPlaylist.add(Playlist("abc","Phan Mạnh Quỳnh",R.drawable.cv_img1,
            Category("abc","Nhạc giực")))
        listPlaylist.add(Playlist("abc","Phan Mạnh Quỳnh",R.drawable.cv_img1,
            Category("abc","Nhạc giực")))
        return listPlaylist
    }
}