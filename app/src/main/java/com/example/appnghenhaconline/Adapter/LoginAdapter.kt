package com.example.appnghenhaconline.Adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.appnghenhaconline.Fragment.LoginTabFragment
import com.example.appnghenhaconline.Fragment.SignupTabFragment

class LoginAdapter(fragmentActivity: FragmentActivity) :
    FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        when(position){
            0 -> {
                return LoginTabFragment()
            }
            1 -> {
                return SignupTabFragment()
            }
            else -> return LoginTabFragment()
        }
    }

}