package com.example.appnghenhaconline.dataLocalManager.SharedPreferences

import android.content.Context

class MySharePreferences(context: Context) {

    companion object{
        private const val KEY_MY_SHARE_PREFERENCES = "MY_SHARE_PREFERENCES"
    }
    private val preferences = context.getSharedPreferences(
        KEY_MY_SHARE_PREFERENCES,
        Context.MODE_PRIVATE)

    //get-set string
    fun getStringValue(key: String): String?{
        return preferences.getString(key, "")
    }

    fun setStringValue(key: String, value: String){
        val editor = preferences.edit()
        editor.putString(key, value)
        editor.apply()
    }

    fun getBooleanValue(key: String): Boolean {
        return preferences.getBoolean(key, false)
    }

    fun setBooleanValue(key: String, value: Boolean){
        val editor = preferences.edit()
        editor.putBoolean(key, value)
        editor.apply()
    }
}