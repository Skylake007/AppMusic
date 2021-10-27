package com.example.appnghenhaconline

import android.content.Context
import android.util.Log
import android.widget.Toast


class MyLib {
    companion object{

        fun showLog(message:String) = Log.e("Log over here: ", message)

        fun showToast(context: Context, message: String) = Toast.makeText(context,
                                                message, Toast.LENGTH_SHORT).show()
    }
}