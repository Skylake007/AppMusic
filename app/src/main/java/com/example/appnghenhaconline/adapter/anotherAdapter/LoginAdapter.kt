package com.example.appnghenhaconline.adapter.anotherAdapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appnghenhaconline.fragment.login.TabLoginFragment
import com.example.appnghenhaconline.fragment.login.TabSignupFragment

class LoginAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> {
                TabLoginFragment()
            }
            1 -> {
                TabSignupFragment()
            }
            else -> TabLoginFragment()
        }
    }

}