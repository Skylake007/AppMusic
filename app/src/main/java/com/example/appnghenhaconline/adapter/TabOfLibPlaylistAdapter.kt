package com.example.appnghenhaconline.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appnghenhaconline.fragment.Library.Playlist.FollowPlaylistFragment
import com.example.appnghenhaconline.fragment.Library.Playlist.MyPlaylistFragment
import com.example.appnghenhaconline.fragment.SingerInfo.AlbumOfSingerFragment
import com.example.appnghenhaconline.fragment.SingerInfo.SongOfSingerFragment

class TabOfLibPlaylistAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity)  {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                MyPlaylistFragment()
            }
            1 -> {
                FollowPlaylistFragment()
            }
            else -> MyPlaylistFragment()
        }
    }

}