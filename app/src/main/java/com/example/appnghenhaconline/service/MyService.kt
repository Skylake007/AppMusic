package com.example.appnghenhaconline.service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.PendingIntent.getBroadcast
import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.net.Uri

import android.os.Bundle
import android.os.IBinder
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.activity.HomeActivity
import com.example.appnghenhaconline.activity.PlayMusicActivity
import com.example.appnghenhaconline.models.song.Song
import com.example.appnghenhaconline.service.MyApplication.Companion.CHANNEL_ID
import com.squareup.picasso.Picasso
import java.io.IOException

class MyService : Service() {

    companion object{
        const val ACTION_PAUSE: Int = 1
        const val ACTION_RESUME: Int = 2
        const val ACTION_CLEAR: Int = 3
        const val ACTION_START: Int = 4
        const val ACTION_INFO: Int = 5
    }

    private var mediaPlayer: MediaPlayer? = null
    private lateinit var mSong :Song
    private var isPlaying : Boolean = false
    var pauseLength: Int = 0

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        MyLib.showLog("MyService created")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer!=null){
            mediaPlayer?.release()
            mediaPlayer = null
        }
    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val bundle = intent.extras
        if (bundle != null){
            val song: Song? = bundle.get("item_song") as? Song
            if (song != null) {
                mSong = song

                startMusic(mSong)
                sendNotification(mSong)
            }
        }

        sendActionToActivity(ACTION_START)
        val actionMusic: Int = intent.getIntExtra("action_music_service", 0)
        handleActionMusic(actionMusic)
        return START_NOT_STICKY
    }

    private fun startMusic(song: Song) {
        if (mediaPlayer!=null){
            mediaPlayer?.stop()
            playSong(song.link)
            MyLib.showLog("MyService: True")
        }else{
            playSong(song.link)
            MyLib.showLog("MyService: False")
        }
        isPlaying = true
        sendActionToActivity(ACTION_START)
    }

    private fun playSong(url : String) { //funtion play music
        try {
            mediaPlayer = MediaPlayer().apply {
                setAudioAttributes(
                    AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                        .setUsage(AudioAttributes.USAGE_MEDIA)
                        .build()
                )
                setDataSource(url)
                setOnPreparedListener { mp -> mp?.start() }
                prepare()
            }

        }catch (e: IOException){
            e.printStackTrace()
        }
    }
    private fun handleActionMusic(action: Int){
        when(action){
            ACTION_PAUSE->{
                pauseMusic()
            }
            ACTION_RESUME->{
                resumeMusic()
            }
            ACTION_CLEAR->{
                stopSelf()
            }
            ACTION_INFO->{
                infoMusic()
            }
        }
    }

    private fun infoMusic() {
        if(mediaPlayer!=null){
            MyLib.showLog("MyService:  infoMusic")
            sendActionToActivity(ACTION_INFO)
        }
    }

    private fun resumeMusic() {
        if (!isPlaying){
            mediaPlayer?.seekTo(pauseLength)
            mediaPlayer?.start()
            isPlaying = true
            sendNotification(mSong)
            sendActionToActivity(ACTION_RESUME)
        }
    }

    private fun pauseMusic() {
        if (isPlaying){
            mediaPlayer?.pause()
            pauseLength = mediaPlayer!!.currentPosition
            isPlaying = false
            sendNotification(mSong)
            sendActionToActivity(ACTION_PAUSE)
        }
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification(song: Song) {
        val intent = Intent(this, HomeActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(this,
                                        0,
                                        intent,
                                        FLAG_UPDATE_CURRENT)

        val remoteView = RemoteViews(packageName, R.layout.i_layout_notification)

        val notification = NotificationCompat.Builder(this, CHANNEL_ID)
                                            .setSmallIcon(R.drawable.ic_dashboard)
                                            .setContentIntent(pendingIntent)
                                            .setCustomContentView(remoteView)
                                            .setSound(null)
                                            .build()

        remoteView.setTextViewText(R.id.tvNameNotification, song.title)
        Picasso.get().load(song.image).into(remoteView,
                                        R.id.imgNotification,
                                        1,
                                        notification)
        remoteView.setImageViewResource(R.id.btnPlayNotification,
                                        R.drawable.ic_baseline_pause_24)
        if (isPlaying){
            remoteView.setOnClickPendingIntent(R.id.btnPlayNotification,
                                            getPendingIntent(this, ACTION_PAUSE))
            remoteView.setImageViewResource(R.id.btnPlayNotification,
                                            R.drawable.ic_baseline_pause_24)
        }else{
            remoteView.setOnClickPendingIntent(R.id.btnPlayNotification,
                                            getPendingIntent(this, ACTION_RESUME))
            remoteView.setImageViewResource(R.id.btnPlayNotification,
                                            R.drawable.ic_baseline_play_arrow_24)
        }
        startForeground(1, notification)
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    private fun getPendingIntent(context: Context, action: Int): PendingIntent? {
        val intent = Intent(this, MyReceiver::class.java)
        intent.putExtra("action_music", action)

        return getBroadcast(context.applicationContext, action, intent, FLAG_UPDATE_CURRENT)
    }
    private fun sendActionToActivity(action: Int){
        val intent = Intent("send_action_to_activity")
        val bundle = Bundle()

        // gửi dữ liệu đến activity
        bundle.putSerializable("item_song", mSong)
        bundle.putBoolean("status_player", isPlaying)
        bundle.putInt("action_music", action)

        intent.putExtras(bundle)

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
}