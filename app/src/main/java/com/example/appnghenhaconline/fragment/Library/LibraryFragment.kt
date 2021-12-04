package com.example.appnghenhaconline.fragment.Library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.fragment.Library.Album.LibraryAlbumFragment
import com.example.appnghenhaconline.fragment.Library.Playlist.LibraryPlaylistFragment
import com.example.appnghenhaconline.fragment.Library.Singer.LibrarySingerFragment

class LibraryFragment : Fragment() {
    internal lateinit var view: View
    private lateinit var layoutAlbum: LinearLayout
    private lateinit var layoutPlaylist: LinearLayout
    private lateinit var layoutSinger: LinearLayout

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fm_library_fragment, container, false)
        init()
        return view
    }

    fun init(){
        layoutAlbum = view.findViewById(R.id.layoutLibOfAlbum)
        layoutPlaylist = view.findViewById(R.id.layoutLibOfPlaylist)
        layoutSinger = view.findViewById(R.id.layoutLibOfSinger)
        initFragmentLib()
    }

    private fun initFragmentLib(){
        layoutPlaylist.setOnClickListener {
            val layoutFragment = LibraryPlaylistFragment()
            MyLib.changeFragment(requireActivity(), layoutFragment)
        }
        layoutAlbum.setOnClickListener {
            val layoutFragment = LibraryAlbumFragment()
            MyLib.changeFragment(requireActivity(), layoutFragment)
        }
        layoutSinger.setOnClickListener {
            val layoutFragment = LibrarySingerFragment()
            MyLib.changeFragment(requireActivity(), layoutFragment)
        }
    }
}