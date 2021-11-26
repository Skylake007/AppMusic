package com.example.appnghenhaconline.dataLocalManager

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class MyApplication: Application() {

    companion object{
        const val CHANNEL_ID : String = "channel_service"
    }

    override fun onCreate() {
        super.onCreate()

        createChannelNotification()
        MyDataLocalManager.init(applicationContext)
    }

    private fun createChannelNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel(CHANNEL_ID,
                                        "Music App",
                                            NotificationManager.IMPORTANCE_DEFAULT)
            channel.setSound(null, null)

            val manager: NotificationManager = getSystemService(
                                                NotificationManager::class.java)
            manager.createNotificationChannel(channel)

        }
    }
}