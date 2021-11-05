package com.example.appnghenhaconline.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import kotlinx.android.synthetic.main.activity_user_password.*

class UserPasswordActivity : AppCompatActivity() {
    lateinit var btnBack: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_password)
        MyLib.hideSystemUI(window, layoutUserPasswordActivity)
        event()
    }

    private fun event() {
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }
}