package com.example.appnghenhaconline.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.fragment.PlayNowFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.android.synthetic.main.fm_signup_tab_fragment.view.*

class UserInfoActivity : AppCompatActivity() {
    lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        MyLib.hideSystemUI(window, layoutUserInfoActivity)

        event()
    }

    private fun event(){
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}