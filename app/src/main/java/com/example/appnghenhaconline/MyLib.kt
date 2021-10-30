package com.example.appnghenhaconline

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.appnghenhaconline.activity.HomeActivity
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
    }
}