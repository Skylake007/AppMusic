package com.example.appnghenhaconline.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import kotlinx.android.synthetic.main.activity_setting.*

class SettingActivity : AppCompatActivity() {
    lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setting)
        MyLib.hideSystemUI(window, layoutSettingActivity)

        event()
    }

    private fun event(){
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressed()
        }
    }
}