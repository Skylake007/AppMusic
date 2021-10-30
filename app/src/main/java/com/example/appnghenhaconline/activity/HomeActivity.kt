package com.example.appnghenhaconline.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.fragment.*
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.models.user.User
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlin.math.abs

class HomeActivity : AppCompatActivity(), GestureDetector.OnGestureListener{

    lateinit var gestureDetector : GestureDetector

    companion object{
        const val MIN_DISTANCE = 100
        const val MIN_VELOCITY = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        hideSystemUI()
        initBottomNav()
        showPlayMusicFragment()
        val user = intent.getSerializableExtra("User") as? User
        Log.e(null, user?.id!!)
    }

    private fun initBottomNav(){
        //mặc định load trang là PlayNowFragment
        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, PlayNowFragment())
                            .apply {
                                tvFragment.text = "Nghe ngay"
                                imgTopNav.setImageResource(R.drawable.ic_play_circle)
                             }.commit()
        //click chuyển trang
        bottomNav.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment = PlayNowFragment()
            when (item.itemId) {
                R.id.action_play_now -> {
                    selectedFragment = PlayNowFragment()
                    tvFragment.text = "Nghe ngay"
                    imgTopNav.setImageResource(R.drawable.ic_play_circle)
                    Toast.makeText(this@HomeActivity, "Nghe ngay", Toast.LENGTH_SHORT).show()
                }
                R.id.action_discover -> {
                    selectedFragment = DiscoverFragment()
                    tvFragment.text = "Khám phá"
                    imgTopNav.setImageResource(R.drawable.ic_dashboard)
                    Toast.makeText(this@HomeActivity, "Khám phá", Toast.LENGTH_SHORT).show()
                }
                R.id.action_radio -> {
                    selectedFragment = RadioFragment()
                    tvFragment.text = "Radio"
                    imgTopNav.setImageResource(R.drawable.ic_podcasts)
                    Toast.makeText(this@HomeActivity, "Radio", Toast.LENGTH_SHORT).show()
                }
                R.id.action_search -> {
                    selectedFragment = SearchFragment()
                    tvFragment.text = "Tìm kiếm "
                    imgTopNav.setImageResource(R.drawable.ic_search)
                    Toast.makeText(this@HomeActivity, "Tìm kiếm", Toast.LENGTH_SHORT).show()
                }
            }
            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainer, selectedFragment)
                                .commit()
            true
        }
    }

    //Ẩn navigation + status bar + fullscreen
    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, layoutHomeActivity).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat
                                            .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun showSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, true)
        WindowInsetsControllerCompat(window, layoutHomeActivity)
                                        .show(WindowInsetsCompat.Type.systemBars())
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun showPlayMusicFragment(){
        gestureDetector = GestureDetector(this, this)
        playNav.setOnTouchListener { v, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }














    override fun onDown(e: MotionEvent?): Boolean {
//        TODO("Not yet implemented")
        return false
    }

    override fun onShowPress(e: MotionEvent?) {
//        TODO("Not yet implemented")
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        val intent = Intent(this, PlayMusicActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slilde_in_up, R.anim.slilde_out_up)
        return true
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?,
                          distanceX: Float, distanceY: Float): Boolean {
//        TODO("Not yet implemented")
        return false
    }

    override fun onLongPress(e: MotionEvent?) {
        TODO("Not yet implemented")
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?,
                         velocityX: Float, velocityY: Float): Boolean {
        if ( e1!!.y - e2!!.y > MIN_DISTANCE && abs(velocityY) > MIN_VELOCITY){
            MyLib.showToast(this,"TOP")
            val intent = Intent(this, PlayMusicActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slilde_in_up, R.anim.slilde_out_up)
        }

     return false
    }
}