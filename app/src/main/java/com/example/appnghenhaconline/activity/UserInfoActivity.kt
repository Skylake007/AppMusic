package com.example.appnghenhaconline.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.android.synthetic.main.fm_signup_tab_fragment.view.*

class UserInfoActivity : AppCompatActivity() {
    lateinit var btnBack: ImageView
    lateinit var edtSex: AutoCompleteTextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        MyLib.hideSystemUI(window, layoutUserInfoActivity)

        init()
        event()
    }

    private fun init(){
        btnBack = findViewById(R.id.btnBack)
        edtSex = findViewById(R.id.etSex)

        val sex = resources.getStringArray(R.array.sex)
        val arrAdapter = ArrayAdapter(this, R.layout.i_dropdown_sex_item, sex)
        edtSex.setAdapter(arrAdapter)
    }

    private fun event(){
        btnBack.setOnClickListener {
            onBackPressed()
        }
    }
}