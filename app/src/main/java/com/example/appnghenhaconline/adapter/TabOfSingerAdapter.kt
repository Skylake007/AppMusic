package com.example.appnghenhaconline.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appnghenhaconline.fragment.SingerInfo.AlbumOfSingerFragment
import com.example.appnghenhaconline.fragment.SingerInfo.SongOfSingerFragment

class TabOfSingerAdapter(fragmentActivity: FragmentActivity, idSinger: String) :
    FragmentStateAdapter(fragmentActivity)  {

    private val mIdSinger: String = idSinger
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                SongOfSingerFragment(mIdSinger)
            }
            1 -> {
                AlbumOfSingerFragment(mIdSinger)
            }
            else -> SongOfSingerFragment(mIdSinger)
        }
    }

}