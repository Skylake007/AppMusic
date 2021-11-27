package com.example.appnghenhaconline.fragment.Library.Album

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.fragment.Library.LibraryFragment

class LibraryAlbumFragment: Fragment() {
    internal lateinit var view: View
    private lateinit var btnBack: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fm_library_album_fragment, container, false)

        return view
    }

    override fun onStart() {
        super.onStart()
        init()
    }

    private fun init(){
        btnBack = view.findViewById(R.id.btnBack)
        event()
    }

    private fun event(){
        btnBack.setOnClickListener {
            val fragmentLayout = LibraryFragment()
            MyLib.changeFragment(requireActivity(), fragmentLayout)
        }
    }

}