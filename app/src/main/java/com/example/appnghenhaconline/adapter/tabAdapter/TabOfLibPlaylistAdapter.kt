package com.example.appnghenhaconline.adapter.tabAdapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appnghenhaconline.fragment.library.Playlist.TabFollowPlaylistFragment
import com.example.appnghenhaconline.fragment.library.Playlist.TabMyPlaylistFragment

class TabOfLibPlaylistAdapter(fragmentActivity: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentActivity, lifecycle)  {

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