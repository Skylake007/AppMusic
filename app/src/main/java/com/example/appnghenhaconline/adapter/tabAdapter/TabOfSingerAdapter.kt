package com.example.appnghenhaconline.adapter.tabAdapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appnghenhaconline.fragment.singerInfo.TabAlbumOfSingerFragment
import com.example.appnghenhaconline.fragment.singerInfo.TabSongOfSingerFragment

class TabOfSingerAdapter(fragmentActivity: FragmentActivity, idSinger: String) :
    FragmentStateAdapter(fragmentActivity)  {

    private val mIdSinger: String = idSinger
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                TabSongOfSingerFragment(mIdSinger)
            }
            1 -> {
                TabAlbumOfSingerFragment(mIdSinger)
            }
            else -> TabSongOfSingerFragment(mIdSinger)
        }
    }

}