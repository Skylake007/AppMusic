package com.example.appnghenhaconline

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.Toast
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import java.math.BigInteger
import java.security.MessageDigest
import java.util.*


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
//            WindowInsetsControllerCompat(window, view).let { controller ->
//                controller.hide(WindowInsetsCompat.Type.statusBars())
//                controller.hide(WindowInsetsCompat.Type.systemBars())
//                controller.systemBarsBehavior = WindowInsetsControllerCompat
//                    .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
//            }
        }


        // chuyển fragment
        fun changeFragment(activity: FragmentActivity, fragmentLayout: Fragment){
            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, fragmentLayout)
                .addToBackStack(null)
                .commit()
        }

         suspend fun getBitmap(context: Context, data: String): Bitmap{
             val loading = ImageLoader(context)
            val request = ImageRequest.Builder(context)
                .data(data)
                .build()
            val result = (loading.execute(request) as SuccessResult).drawable
            return (result as BitmapDrawable).bitmap
        }
    }
}