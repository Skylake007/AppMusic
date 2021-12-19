package com.example.appnghenhaconline.adapter.tabAdapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appnghenhaconline.fragment.library.Playlist.TabFollowPlaylistFragment
import com.example.appnghenhaconline.fragment.library.Playlist.TabMyPlaylistFragment

class TabOfLibPlaylistAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity)  {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                TabMyPlaylistFragment()
            }
            1 -> {
                TabFollowPlaylistFragment()
            }
            else -> TabMyPlaylistFragment()
        }
    }

}