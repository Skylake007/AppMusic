package com.example.appnghenhaconline.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appnghenhaconline.fragment.*

class HomeAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                PlayNowFragment()
            }
            1 -> {
                DiscoverFragment()
            }
            2 -> {
                RadioFragment()
            }
            3 -> {
                SearchFragment()
            }
            else -> PlayNowFragment()
        }
    }
}