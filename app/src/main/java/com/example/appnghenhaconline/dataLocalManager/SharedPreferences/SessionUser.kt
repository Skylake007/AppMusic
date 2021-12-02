package com.example.appnghenhaconline.dataLocalManager.SharedPreferences

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import android.util.Log
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.activity.HomeActivity
import com.example.appnghenhaconline.activity.LoginActivity
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.models.playlist.Playlist
import com.example.appnghenhaconline.models.user.DataUser
import com.example.appnghenhaconline.models.user.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SessionUser {
    lateinit var pref : SharedPreferences
    lateinit var editor : Editor
    lateinit var context: Context

    var PRIVATE_MODE : Int = 0

    val PREF_NAME = "AndroidUser"
    val IS_LOGIN =  "IsLoggedIn"
    val KEY_ID = "id"
    val KEY_NAME = "name"
    val KEY_EMAIL = "email"
    val KEY_SEX = "sex"
    val KEY_PASSWORD = "password"
    val KEY_PLAYLIST = "playlist"
    val KEY_ALBUM = "album"

    constructor(context: Context) {
        this.context = context
        pref = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE)
        editor = pref.edit()
    }

    fun createLoginSession(id : String, name : String, email : String, sex : Boolean, password : String) {
        editor.putBoolean(IS_LOGIN,true)
        editor.putString(KEY_ID,id)
        editor.putString(KEY_NAME,name)
        editor.putString(KEY_EMAIL,email)
        editor.putBoolean(KEY_SEX,sex)
        editor.putString(KEY_PASSWORD,password)
        editor.commit()
    }

    fun getUserDetails() : HashMap<String, String> {
        var user : HashMap<String, String> = HashMap()

        user[KEY_ID] = pref.getString(KEY_ID,null).toString()

        user[KEY_NAME] = pref.getString(KEY_NAME,null).toString()

        user[KEY_EMAIL] = pref.getString(KEY_EMAIL,null).toString()

        user[KEY_SEX] = pref.getBoolean(KEY_SEX,  true).toString()

        user[KEY_PASSWORD] = pref.getString(KEY_PASSWORD,null).toString()

        user[KEY_PLAYLIST] = pref.getString(KEY_PLAYLIST, null).toString()

        user[KEY_ALBUM] = pref.getString(KEY_ALBUM, null).toString()

        return user
    }

    private fun isLoggedIn() : Boolean {
        return pref.getBoolean(IS_LOGIN, false)
    }

    fun checkLogin() {
        if (!this.isLoggedIn()) {
            var i = Intent(context, LoginActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(i)
        }
    }

    fun logoutUser() {
        editor.clear()
        editor.commit()

        var i = Intent(context, LoginActivity::class.java)
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        context.startActivity(i)
    }

}