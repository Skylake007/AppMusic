package com.example.appnghenhaconline.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.adapter.LoginAdapter
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.dataLocalManager.MyDataLocalManager
import com.example.appnghenhaconline.dataLocalManager.MySharePreferences
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        MyLib.hideSystemUI(window, layoutLoginActivity)
        initTabFragment()

        checkFirstInstalled()
    }

    private fun initTabFragment(){
        val loginAdapter = LoginAdapter(this)
        view_pager.adapter = loginAdapter
        TabLayoutMediator(tab_layout,view_pager){tittle, position ->
            when(position){
                0 ->{
                    tittle.text = "Đăng nhập"
                }
                1 -> {
                    tittle.text = "Đăng ký"
                }
                else -> {
                    tittle.text = "Đăng nhập"
                }
            }
        }.attach()
    }

    private fun checkFirstInstalled() {
        if (!MyDataLocalManager.getFirstInstalled()){
            MyLib.showToast(this,"Is First Installed")
            MyDataLocalManager.setFirstInstalled(true)
        }
    }
}