package com.example.appnghenhaconline.SharedPreferences

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.SharedPreferences.Editor
import com.example.appnghenhaconline.activity.LoginActivity

class SessionUser {
    lateinit var pref : SharedPreferences
    lateinit var editor : Editor
    lateinit var context: Context

    var PRIVATE_MODE : Int = 0

    val PREF_NAME = "AndroidUser"
    val IS_LOGIN =  "IsLoggedIn"
    val KEY_NAME = "name"
    val KEY_EMAIL = "email"
    val KEY_SEX = "sex"

    constructor(context: Context) {
        this.context = context
        pref = context.getSharedPreferences(PREF_NAME,PRIVATE_MODE)
        editor = pref.edit()
    }

    fun createLoginSession(name : String, email : String, sex : Boolean) {
        editor.putBoolean(IS_LOGIN,true)
        editor.putString(KEY_NAME,name)
        editor.putString(KEY_EMAIL,email)
        editor.putBoolean(KEY_SEX,sex)

        editor.commit()
    }

    fun getUserDetails() : HashMap<String, String> {
        var user : HashMap<String, String> = HashMap()

        user[KEY_NAME] = pref.getString(KEY_NAME,null)!!

        user[KEY_EMAIL] = pref.getString(KEY_EMAIL,null)!!

        user[KEY_SEX] = pref.getBoolean(KEY_SEX,  true).toString()

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