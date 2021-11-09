package com.example.appnghenhaconline.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.adapter.LoginAdapter
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.SharedPreferences.SessionUser
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        MyLib.hideSystemUI(window, layoutLoginActivity)
        initTabFragment()
    }

    private fun initTabFragment(){
        val loginAdapter = LoginAdapter(this)
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