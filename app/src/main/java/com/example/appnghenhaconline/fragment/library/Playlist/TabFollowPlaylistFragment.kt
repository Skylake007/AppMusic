package com.example.appnghenhaconline.fragment.library.Playlist

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.adapter.playlistAdapter.FollowPlaylistAdapter
import com.example.appnghenhaconline.dataLocalManager.SharedPreferences.SessionUser
import com.example.appnghenhaconline.models.playlist.Playlist
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class TabFollowPlaylistFragment : Fragment(){
    internal lateinit var view: View
    private lateinit var listFollowPlaylist: ArrayList<Playlist>
    private lateinit var rcvFollowPlaylist: RecyclerView
    private lateinit var followPlaylistAdapter: FollowPlaylistAdapter
    private lateinit var session: SessionUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        view = inflater.inflate(R.layout.tab_follow_playlist, container, false)
        init()
        return view
    }

    private fun init(){
        session = SessionUser(this.requireContext())
        initFollowPlaylist()
    }

    //region INIT ADAPTER
    private fun initFollowPlaylist(){
        listFollowPlaylist = ArrayList()
        followPlaylistAdapter = FollowPlaylistAdapter(view.context, listFollowPlaylist)

        rcvFollowPlaylist =view.findViewById(R.id.rcvFollowPlaylist)
        rcvFollowPlaylist.setHasFixedSize(true)
        rcvFollowPlaylist.layoutManager = GridLayoutManager(view.context,2)
        rcvFollowPlaylist.adapter = followPlaylistAdapter

        showFollowPlaylist(listFollowPlaylist, followPlaylistAdapter)
    }

    private fun showFollowPlaylist(list : ArrayList<Playlist>, adapter : FollowPlaylistAdapter) {
        val getPlaylist = session.getUserDetails()
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<Playlist?>?>() {}.type
        val playlistFollow : ArrayList<Playlist> = gson.fromJson(getPlaylist[session.KEY_PLAYLIST],type)
        list.addAll(playlistFollow)
        adapter.notifyDataSetChanged()
    }
}