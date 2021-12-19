package com.example.appnghenhaconline.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.adapter.anotherAdapter.LoginAdapter
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.dataLocalManager.MyDataLocalManager
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_login.*
import android.content.Intent




class LoginActivity : AppCompatActivity() {

    private var backPressedTime: Long = 0
    private lateinit var backToast: Toast
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_login)
        MyLib.hideSystemUI(window, layoutLoginActivity)
        initTabFragment()

        checkFirstInstalled()
    }

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel()

            val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(a)
        }else{
            backToast = Toast.makeText(this,"Chạm 2 lần để thoát ứng dụng !!!",Toast.LENGTH_SHORT)
            backToast.show()
        }
        backPressedTime = System.currentTimeMillis()
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