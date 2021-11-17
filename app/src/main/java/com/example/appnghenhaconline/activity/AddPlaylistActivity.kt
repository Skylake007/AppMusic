package com.example.appnghenhaconline.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.fragment.LibraryPlaylistFragment

class AddPlaylistActivity : AppCompatActivity() {
    lateinit var btnBack: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_playlist)
        init()
    }

    private fun init(){
        btnBack = findViewById(R.id.btnBack)
        event()
    }

    private fun event() {
        btnBack.setOnClickListener {

        }
    }
}