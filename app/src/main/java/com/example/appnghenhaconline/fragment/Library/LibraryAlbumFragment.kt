package com.example.appnghenhaconline.fragment.Library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.appnghenhaconline.R

class LibraryAlbumFragment: Fragment() {
    internal lateinit var view: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fm_library_album_fragment, container, false)

        return view
    }
}