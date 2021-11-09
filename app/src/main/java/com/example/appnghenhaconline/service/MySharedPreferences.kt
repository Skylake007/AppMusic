package com.example.appnghenhaconline.service

import android.content.Context
import android.content.SharedPreferences

class MySharedPreferences(val context: Context) {
    companion object{
        private const val KEY_SHARED_PREFERENCES = "SHARED_PREFERENCES"
    }

    private fun putBooleanValue(key: String, value: Boolean){
        val sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES,
                                                            Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPreferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }

    private fun getBooleanValue(key: String): Boolean{
        val sharedPreferences = context.getSharedPreferences(KEY_SHARED_PREFERENCES,
            Context.MODE_PRIVATE)
        return sharedPreferences.getBoolean(key, false)
    }
}