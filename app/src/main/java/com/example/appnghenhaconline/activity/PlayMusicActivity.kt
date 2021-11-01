package com.example.appnghenhaconline.activity

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.models.user.User
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_play_music.*
import kotlin.math.abs


class PlayMusicActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    lateinit var gestureDetector : GestureDetector

    companion object{
        const val MIN_DISTANCE = 100
        const val MIN_VELOCITY = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_music)
        hideSystemUI()
        actionAnim()
        swipeActivity()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slilde_in_down, R.anim.slilde_out_down)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun swipeActivity(){
        gestureDetector = GestureDetector(this, this)
        layoutPlayMusic.setOnTouchListener { v, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }


    private fun actionAnim() {
        btnStartMusic.setOnClickListener {
            if (btnStartMusic.isVisible){
                val animZoomIn = AnimationUtils.loadAnimation(this, R.anim.anim_zoom_in_img)
                imgPlayMusic.startAnimation(animZoomIn)
                btnStartMusic.visibility = View.GONE
                btnPauseMusic.visibility = View.VISIBLE
            }
        }
        btnPauseMusic.setOnClickListener {
            if (btnPauseMusic.isVisible){
                val animZoomOut = AnimationUtils.loadAnimation(this, R.anim.anim_zoom_out_img)
                imgPlayMusic.startAnimation(animZoomOut)
                btnPauseMusic.visibility = View.GONE
                btnStartMusic.visibility = View.VISIBLE
            }
        }
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, layoutPlayMusic).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat
                .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return false
    }

    override fun onShowPress(e: MotionEvent?) {

    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?,
                          distanceX: Float, distanceY: Float): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent?) {

    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?,
                         velocityX: Float, velocityY: Float): Boolean {
        if ( e1!!.y - e2!!.y < MIN_DISTANCE && abs(velocityY) > MIN_VELOCITY){
            MyLib.showToast(this,"Bottom")
            onBackPressed()
        }
        return false
    }
}