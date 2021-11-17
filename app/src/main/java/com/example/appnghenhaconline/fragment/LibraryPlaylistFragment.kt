package com.example.appnghenhaconline.fragment

import android.content.Intent
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
import com.example.appnghenhaconline.activity.AddPlaylistActivity
import com.example.appnghenhaconline.adapter.FollowPlaylistAdapter
import com.example.appnghenhaconline.adapter.PlaylistSMAdapter
import com.example.appnghenhaconline.adapter.SongAdapter
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.dataLocalManager.SharedPreferences.SessionUser
import com.example.appnghenhaconline.models.playlist.DataPlayList
import com.example.appnghenhaconline.models.playlist.Playlist
import com.example.appnghenhaconline.models.song.DataSong
import com.example.appnghenhaconline.models.song.Song
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class LibraryPlaylistFragment: Fragment() {
    internal lateinit var view: View
    private lateinit var btnAddPlaylist: ImageView
    lateinit var listFollowPlaylist: ArrayList<Playlist>
    lateinit var rcvFollowPlaylist: RecyclerView
    lateinit var followPlaylistAdapter: FollowPlaylistAdapter
    lateinit var session: SessionUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fm_library_playlist_fragment, container, false)

        return view
    }

    override fun onStart() {
        super.onStart()
        session = SessionUser(this.requireContext())
        init()
        event()
    }

    private fun init(){
        btnAddPlaylist = view.findViewById(R.id.btnAddPlaylist)
        initFollowPlaylist()
    }

    private fun event(){
        btnAddPlaylist.setOnClickListener {
            val intent = Intent(requireContext(),AddPlaylistActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initFollowPlaylist(){
        listFollowPlaylist = ArrayList()
        followPlaylistAdapter = FollowPlaylistAdapter(view.context, listFollowPlaylist)

        rcvFollowPlaylist =view.findViewById(R.id.rcvFollowPlaylist)
        rcvFollowPlaylist.setHasFixedSize(true)
        rcvFollowPlaylist.layoutManager = LinearLayoutManager(view.context,
                    LinearLayoutManager.HORIZONTAL, false)
        rcvFollowPlaylist.adapter = followPlaylistAdapter

        showFollowPlaylist(listFollowPlaylist, followPlaylistAdapter)
    }

    private fun showFollowPlaylist(list : ArrayList<Playlist>, adapter : FollowPlaylistAdapter) {
        var getPlaylist = session.getUserDetails()
        var gson = Gson()
        val type: Type = object : TypeToken<ArrayList<Playlist?>?>() {}.type
        var playlistFollow : ArrayList<Playlist> = gson.fromJson(getPlaylist[session.KEY_PLAYLIST],type)
        list.addAll(playlistFollow)
        adapter.notifyDataSetChanged()

    }

}