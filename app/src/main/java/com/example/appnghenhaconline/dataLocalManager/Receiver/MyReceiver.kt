package com.example.appnghenhaconline.dataLocalManager.Receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.appnghenhaconline.dataLocalManager.Service.MyService

class MyReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        //nhận action từ MyService
        val actionMusic: Int = intent.getIntExtra("action_music", 0)
        //trả action về MyService
        val intentService = Intent(context, MyService::class.java)
        intentService.putExtra("action_music_service", actionMusic)

        context.startService(intentService)
    }
}