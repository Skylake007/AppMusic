package com.example.appnghenhaconline.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class MyReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        //nhận action từ MysService
        val actionMusic: Int = intent.getIntExtra("action_music", 0)
        //trả action về MysService
        val intentService = Intent(context, MyService::class.java)
        intentService.putExtra("action_music_service", actionMusic)

        context.startService(intentService)
    }
}