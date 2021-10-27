package com.example.appnghenhaconline.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import com.example.appnghenhaconline.R
import kotlinx.android.synthetic.main.activity_play_music.*


class PlayMusicActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_music)
        hideSystemUI()
        actionAnim()
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
}