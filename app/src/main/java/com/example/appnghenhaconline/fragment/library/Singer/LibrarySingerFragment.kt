package com.example.appnghenhaconline.fragment.library.Singer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.adapter.FollowAlbumAdapter
import com.example.appnghenhaconline.adapter.FollowPlaylistAdapter
import com.example.appnghenhaconline.adapter.MyPlaylistAdapter
import com.example.appnghenhaconline.adapter.PlaylistSelectedAdapter
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.dataLocalManager.SharedPreferences.SessionUser
import com.example.appnghenhaconline.fragment.library.LibraryFragment
import com.example.appnghenhaconline.models.album.Album
import com.example.appnghenhaconline.models.playlist.DataPlayListUser
import com.example.appnghenhaconline.models.playlist.PlayListUser
import com.example.appnghenhaconline.models.playlist.Playlist
import com.example.appnghenhaconline.models.singer.DataSinger
import com.example.appnghenhaconline.models.singer.Singer
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class LibrarySingerFragment: Fragment() {
    internal lateinit var view: View
    private lateinit var btnBack: ImageView
    private lateinit var listFollowAlbum: ArrayList<Album>
    private lateinit var rcvFollowPlaylist: RecyclerView
    private lateinit var followAlbumAdapter: FollowAlbumAdapter
    private lateinit var session: SessionUser


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fm_library_album_fragment, container, false)

        return view
    }

    override fun onStart() {
        super.onStart()
        session = SessionUser(this.requireContext())
        init()
    }

    private fun init(){
        btnBack = view.findViewById(R.id.btnBack)
        initFollowPlaylist()
        event()
    }

    private fun event(){
        btnBack.setOnClickListener {
            val fragmentLayout = LibraryFragment()
            MyLib.changeFragment(requireActivity(), fragmentLayout)
        }
    }

    private fun initFollowPlaylist(){
        listFollowAlbum = ArrayList()
        followAlbumAdapter = FollowAlbumAdapter(view.context, listFollowAlbum)

        rcvFollowPlaylist =view.findViewById(R.id.rcvFollowAlbum)
        rcvFollowPlaylist.setHasFixedSize(true)
        rcvFollowPlaylist.layoutManager = GridLayoutManager(view.context, 2)
        rcvFollowPlaylist.adapter = followAlbumAdapter

        var user = session.getUserDetails()
//        user[session.KEY_SINGER]
        //Nghĩa lấy cái hàm user[session.KEY_SINGER] truyền vào cuối cái call Api nha
//        callApiLoadPlayListUser()
    }

    // Api lấy list Singer yêu thich
    private fun callApiListLoveSinger(list : ArrayList<Singer>, adapter : PlaylistSelectedAdapter, listIdUser : ArrayList<String>) {
        ApiService.apiService.getListLoveSinger(listIdUser).enqueue(object : Callback<DataSinger?> {
            override fun onResponse(call: Call<DataSinger?>, response: Response<DataSinger?>) {
                val dataSinger = response.body()
                if(dataSinger != null) {
                    if (!dataSinger.error) {
                        val dataListSiger = dataSinger.singers

                        list.addAll(dataListSiger)
                        adapter.notifyDataSetChanged()
                    }
                    else {
                        MyLib.showToast(requireContext(),dataSinger.message)
                    }
                }
            }

            override fun onFailure(call: Call<DataSinger?>, t: Throwable) {
                MyLib.showToast(requireContext(),"Call Api Error" )
            }
        })
    }
}