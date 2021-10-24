package com.example.appnghenhaconline.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.viewpager2.widget.ViewPager2
import com.example.appnghenhaconline.Adapter.HomeAdapter
import com.example.appnghenhaconline.Adapter.LoginAdapter
import com.example.appnghenhaconline.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_login.*

class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        initBottomNav()
    }

    private fun initViewPager(){
        var homeAdapter = HomeAdapter(this)
        viewPager.adapter = homeAdapter

        viewPager.registerOnPageChangeCallback(object: ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    0 -> {
                        tvFragment.text = "Nghe ngay"
                        imgTopNav.setImageResource(R.drawable.ic_play_circle)
                        bottomNav.menu.findItem(R.id.action_play_now).setChecked(true)
                    }
                    1 -> {
                        tvFragment.text = "Khám phá"
                        imgTopNav.setImageResource(R.drawable.ic_dashboard)
                        bottomNav.menu.findItem(R.id.action_discover).setChecked(true)
                    }
                    2 -> {
                        tvFragment.text = "Radio"
                        imgTopNav.setImageResource(R.drawable.ic_podcasts)
                        bottomNav.menu.findItem(R.id.action_radio).setChecked(true)
                    }
                    3 -> {
                        tvFragment.text = "Tìm kiếm "
                        imgTopNav.setImageResource(R.drawable.ic_search)
                        bottomNav.menu.findItem(R.id.action_search).setChecked(true)
                    }
                }
            }
        })
    }
    private fun initBottomNav(){

        initViewPager()
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_play_now -> {
                    viewPager.currentItem = 0
                    Toast.makeText(this@HomeActivity, "Nghe ngay", Toast.LENGTH_SHORT).show()
                }
                R.id.action_discover -> {
                    viewPager.currentItem = 1
                    Toast.makeText(this@HomeActivity, "Khám phá", Toast.LENGTH_SHORT).show()
                }
                R.id.action_radio -> {
                    viewPager.currentItem = 2
                    Toast.makeText(this@HomeActivity, "Radio", Toast.LENGTH_SHORT).show()
                }
                R.id.action_search -> {
                    viewPager.currentItem = 3
                    Toast.makeText(this@HomeActivity, "Tìm kiếm", Toast.LENGTH_SHORT).show()
                }
            }
            true
        }


    }
}