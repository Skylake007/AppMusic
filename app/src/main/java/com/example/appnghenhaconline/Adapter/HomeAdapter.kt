package com.example.appnghenhaconline.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appnghenhaconline.Fragment.*

class HomeAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 4
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> {
                return PlayNowFragment()
            }
            1 -> {
                return DiscoverFragment()
            }
            2 -> {
                return RadioFragment()
            }
            3 -> {
                return SearchFragment()
            }
            else -> return PlayNowFragment()
        }
    }
}