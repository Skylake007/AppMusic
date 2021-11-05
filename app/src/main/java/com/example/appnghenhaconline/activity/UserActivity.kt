package com.example.appnghenhaconline.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {
    lateinit var viewInfo: LinearLayout
    lateinit var viewChangePass: LinearLayout
    lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        MyLib.hideSystemUI(window, layoutUserActivity)

        init()
        event()
    }

    private fun init(){
        viewInfo = findViewById(R.id.layout_info)
        viewChangePass = findViewById(R.id.layout_password)
        btnBack = findViewById(R.id.btnBack)
    }

    private fun event(){
        var intent: Intent
        viewInfo.setOnClickListener {
            intent = Intent(this, UserInfoActivity::class.java)
            startActivity(intent)
        }
        viewChangePass.setOnClickListener {
            intent = Intent(this, UserPasswordActivity::class.java)
            startActivity(intent)
        }
        btnBack.setOnClickListener {
            onBackPressed()
        }
    }
}