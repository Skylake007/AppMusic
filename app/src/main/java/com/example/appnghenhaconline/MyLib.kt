package com.example.appnghenhaconline

import android.content.Context
import android.media.MediaPlayer
import android.text.Layout
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.example.appnghenhaconline.activity.HomeActivity
import kotlinx.android.synthetic.main.activity_home.*
import java.io.IOException
import java.math.BigInteger
import java.security.MessageDigest



class MyLib {
    companion object{

        fun showLog(message:String) = Log.e("Log over here: ", message)

        fun showToast(context: Context, message: String) = Toast.makeText(context,
                                                message, Toast.LENGTH_SHORT).show()
        fun md5(input:String): String {
            val md = MessageDigest.getInstance("MD5")
            return BigInteger(1, md.digest(input.toByteArray())).toString(16).padStart(32, '0')
        }


        //Ẩn navigation + status bar + fullscreen
        fun hideSystemUI(window: Window, view: View) {
            WindowCompat.setDecorFitsSystemWindows(window, false)
            WindowInsetsControllerCompat(window, view).let { controller ->
                controller.hide(WindowInsetsCompat.Type.statusBars())
//            controller.hide(WindowInsetsCompat.Type.systemBars())
                controller.systemBarsBehavior = WindowInsetsControllerCompat
                    .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
            }
        }
    }
}