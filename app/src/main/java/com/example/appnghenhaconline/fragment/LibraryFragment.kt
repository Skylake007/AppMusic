package com.example.appnghenhaconline.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.example.appnghenhaconline.R

class LibraryFragment : Fragment() {
    internal lateinit var view: View
    lateinit var layoutAlbum: LinearLayout
    lateinit var layoutArtist: LinearLayout
    lateinit var layoutPlaylist: LinearLayout
    lateinit var layoutSong: LinearLayout

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fm_library_fragment, container, false)
        init()
        return view
    }

    fun init(){
        layoutAlbum = view.findViewById(R.id.layoutLibOfAlbum)
        layoutArtist = view.findViewById(R.id.layoutLibOfArtist)
        layoutPlaylist = view.findViewById(R.id.layoutLibOfPlaylist)
        layoutSong = view.findViewById(R.id.layoutLibOfSong)
        initFragmentLib()
    }

    private fun initFragmentLib(){
        layoutPlaylist.setOnClickListener {
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainer, LibraryPlaylistFragment())
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }
        layoutArtist.setOnClickListener {
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainer, LibraryArtistFragment())
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }
        layoutAlbum.setOnClickListener {
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainer, LibraryAlbumFragment())
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }
        layoutSong.setOnClickListener {
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainer, LibrarySongFragment())
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }
    }
}