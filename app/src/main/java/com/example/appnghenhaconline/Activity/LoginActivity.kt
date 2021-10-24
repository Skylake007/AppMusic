package com.example.appnghenhaconline.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.FLAG_FULLSCREEN
import android.widget.TableLayout
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.example.appnghenhaconline.Adapter.LoginAdapter
import com.example.appnghenhaconline.Fragment.LoginTabFragment
import com.example.appnghenhaconline.Fragment.SignupTabFragment
import com.example.appnghenhaconline.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        window.setFlags(FLAG_FULLSCREEN, FLAG_FULLSCREEN)
        setContentView(R.layout.activity_login)

        initTabFragment()
    }

    private fun initTabFragment(){
        var loginAdapter = LoginAdapter(this)
        view_pager.adapter = loginAdapter
        TabLayoutMediator(tab_layout,view_pager){tittle, position ->
            when(position){
                0 ->{
                    tittle.text = "Login"
                }
                1 -> {
                    tittle.text = "Sign Up"
                }
                else -> {
                    tittle.text = "Login"
                }
            }
        }.attach()
    }
}