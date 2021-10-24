package com.example.appnghenhaconline.Activity

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.example.appnghenhaconline.R
import kotlinx.android.synthetic.main.activity_play_music.*

class PlayMusicActivity : AppCompatActivity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_music)

        actionAnim()
    }

    private fun actionAnim() {
        btnStartMusic.setOnClickListener {
            playAnim()
            if (btnStartMusic.isVisible){
                btnStartMusic.visibility = View.GONE
                btnPauseMusic.visibility = View.VISIBLE
            }
        }
        btnPauseMusic.setOnClickListener {
            pauseAnim()
            if (btnPauseMusic.isVisible){
                btnPauseMusic.visibility = View.GONE
                btnStartMusic.visibility = View.VISIBLE
            }
        }
    }

    private fun playAnim() {
//        var animRotate = AnimationUtils.loadAnimation(this, R.anim.anim_rotate_img)
//        animRotate.interpolator = LinearInterpolator()
//        imgPlayMusic.startAnimation(animRotate)

//        val runnable = Runnable {
//            run {
//                imgPlayMusic.animate().rotationBy(360f)
//                    .withEndAction { this }
//                    .setDuration(1000)
//                    .setInterpolator(LinearInterpolator())
//                    .start()
//            }
//        }
//        imgPlayMusic.animate().rotationBy(360f)
//            .withEndAction{runnable.run()}
//            .setDuration(1000)
//            .setInterpolator(LinearInterpolator())
//            .start()
    }

    private fun pauseAnim() {
        imgPlayMusic.animate().cancel()
    }



}