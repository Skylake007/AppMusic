package com.example.appnghenhaconline.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.appnghenhaconline.R

class LibraryArtistFragment: Fragment() {
    internal lateinit var view: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fm_library_artist_fragment, container, false)

        return view
    }
}