package com.example.appnghenhaconline.fragment.Library.Album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.adapter.FollowAlbumAdapter
import com.example.appnghenhaconline.adapter.FollowPlaylistAdapter
import com.example.appnghenhaconline.adapter.MyPlaylistAdapter
import com.example.appnghenhaconline.dataLocalManager.SharedPreferences.SessionUser
import com.example.appnghenhaconline.fragment.Library.LibraryFragment
import com.example.appnghenhaconline.models.album.Album
import com.example.appnghenhaconline.models.playlist.PlayListUser
import com.example.appnghenhaconline.models.playlist.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class LibraryAlbumFragment: Fragment() {
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
        rcvFollowPlaylist.layoutManager = LinearLayoutManager(view.context,
            LinearLayoutManager.HORIZONTAL, false)
        rcvFollowPlaylist.adapter = followAlbumAdapter

//        showFollowPlaylist(listFollowPlaylist, followPlaylistAdapter)
    }

//    private fun showFollowPlaylist(list : ArrayList<Album>, adapter : FollowAlbumAdapter) {
//        val getPlaylist = session.getUserDetails()
//        val gson = Gson()
//        val type: Type = object : TypeToken<ArrayList<Playlist?>?>() {}.type
//        val playlistFollow : ArrayList<Playlist> = gson.fromJson(getPlaylist[session.KEY_PLAYLIST],type)
//        list.addAll(playlistFollow)
//        adapter.notifyDataSetChanged()
//    }
}