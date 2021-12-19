package com.example.appnghenhaconline.dataLocalManager.Service

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.app.PendingIntent.getBroadcast
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.session.MediaSession
import android.media.session.MediaSession.Token
import android.os.Bundle
import android.os.IBinder
import android.support.v4.media.session.MediaSessionCompat
import android.util.Base64
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.activity.PlayMusicActivity
import com.example.appnghenhaconline.activity.PlayMusicActivity.Companion.isRepeat
import com.example.appnghenhaconline.activity.PlayMusicActivity.Companion.isShuffle
import com.example.appnghenhaconline.models.song.Song
import com.example.appnghenhaconline.dataLocalManager.MyApplication.Companion.CHANNEL_ID
import com.example.appnghenhaconline.dataLocalManager.MyDataLocalManager
import com.example.appnghenhaconline.dataLocalManager.Receiver.MyReceiver
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

class MyService : Service() {
    companion object{
        const val ACTION_PAUSE: Int = 1
        const val ACTION_RESUME: Int = 2
        const val ACTION_CLEAR: Int = 3
        const val ACTION_START: Int = 4
        const val ACTION_NEXT: Int = 5
        const val ACTION_PREVIOUS: Int = 6
        const val ACTION_REPEAT: Int = 7
        const val ACTION_SHUFFLE: Int = 8
        var mediaPlayer: MediaPlayer = MediaPlayer()
    }
    private var mList : ArrayList<Song> = ArrayList()
    private var isPlaying : Boolean = false
    var pauseLength: Int = 0
    var mPosition: Int = -1

    //region LIFE CIRCLE
    //rằng buộc service
    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    //khởi tạo service
    override fun onCreate() {
        super.onCreate()
    }

    //run service
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {

        val bundle = intent.extras
        if (bundle != null){
            //Nhận dữ liệu bài hát
            val position: Int? = bundle.get("position_song") as? Int
            val list: ArrayList<Song>? = bundle.get("list_song") as? ArrayList<Song>
            if (list != null && position != null) {
                mList = list
                mPosition = position

                startMusic(mList[mPosition])
                sendNotification(mList[mPosition])
            }
        }

        sendActionToActivity(ACTION_START)
        val actionMusic: Int = intent.getIntExtra("action_music_service", 0)
        handleActionMusic(actionMusic)

        return START_NOT_STICKY
    }

    //hủy service
    override fun onDestroy() {
        super.onDestroy()
        if (mediaPlayer!=null){
            mediaPlayer.release()
        }
    }
    //endregion
    //====================================================
    //region XỬ LÝ ACTION
    // nhận action từ activity và xử lý
    private fun handleActionMusic(action: Int){
        when(action){
            ACTION_PAUSE ->{
                pauseMusic()
            }
            ACTION_RESUME ->{
                resumeMusic()
            }
            ACTION_CLEAR ->{
                stopMusic()
            }
            ACTION_NEXT ->{
                nextMusic()
            }
            ACTION_PREVIOUS ->{
                previousMusic()
            }
            ACTION_REPEAT ->{
                repeatMusic()
            }
            ACTION_SHUFFLE ->{
                shuffleMusic()
            }
        }
    }

    private fun stopMusic() {
        mediaPlayer.stop()
        mediaPlayer.release()
        stopSelf()
    }

    // phát nhạc
    private fun startMusic(song: Song) {
        if (isPlaying){
            mediaPlayer.stop()
            mediaPlayer.release()
        }
        playSong(song.link)
        isPlaying = true
    }

    // khởi tạo media player
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

    // phát tiếp bài hát đang hát
    private fun resumeMusic() {
        if (!isPlaying){
//            mediaPlayer.seekTo(pauseLength)
            mediaPlayer.start()
            isPlaying = true
            sendNotification(mList[mPosition])
            sendActionToActivity(ACTION_RESUME)
        }
    }

    // tạm dừng bài hát
    private fun pauseMusic() {
        if (isPlaying){
            mediaPlayer.pause()
//            pauseLength = mediaPlayer.currentPosition

            isPlaying = false
            sendNotification(mList[mPosition])
            sendActionToActivity(ACTION_PAUSE)
        }
    }

    // lùi về bài hát trước
    private fun previousMusic() {
        nextPrevMusic(isNext = false)
        sendNotification(mList[mPosition])
        sendActionToActivity(ACTION_PREVIOUS)
    }

    // next tới bài hát kế tiếp
    private fun nextMusic() {
        nextPrevMusic()
        sendNotification(mList[mPosition])
        sendActionToActivity(ACTION_NEXT)
    }

    // hàm xử lý tiến & lùi
    private fun nextPrevMusic(isNext: Boolean = true){
        if (isNext) setPosition()
        else setPosition(isIncrement = false)
        startMusic(mList[mPosition])
    }

    // xáo trộn bài hát
    private fun shuffleMusic() {
        sendActionToActivity(ACTION_SHUFFLE)
    }

    // lặp lại bài hát
    private fun repeatMusic() {
        mediaPlayer.isLooping = true
        startMusic(mList[mPosition])
        sendActionToActivity(ACTION_REPEAT)
    }

    // random 1 số ngẫu nhiên
    private fun randomPosition(i: Int): Int{
        val random = Random()
        return random.nextInt(i + 1)
    }

    // set position cho bài hát (có xáo trộn bài hát)
    private fun setPosition(isIncrement : Boolean = true){
        if (isIncrement){           //nếu next
            if (isShuffle){         //nếu xáo trộn
                val mPosBef = mPosition
                mPosition = randomPosition(mList.size - 1)
                if (mPosition == mPosBef) mPosition = randomPosition(mList.size - 1)
            }else{
                if (mList.size -1 == mPosition) mPosition = 0
                else ++mPosition
            }
        }else{          //nếu prev
            if (mPosition == 0) mPosition = mList.size -1
            else --mPosition
        }
    }

    //endregion
    //====================================================
    //region GỬI Notification
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun sendNotification(song: Song){
        val scope = CoroutineScope(Job()+ Dispatchers.Main)
        scope.launch {
            val intent = Intent(this@MyService, PlayMusicActivity::class.java)
            val pendingIntent = PendingIntent.getActivity(this@MyService,
                                                            0,
                                                            intent,
                                                            FLAG_UPDATE_CURRENT)

            val imageSinger = MyLib.getBitmap(this@MyService, song.image)

            val mMediaSession  = MediaSessionCompat(this@MyService, "tag")
            mMediaSession.isActive = true
            val style = androidx.media.app.NotificationCompat.MediaStyle()
            style.setShowActionsInCompactView(0, 1, 2)
                .setMediaSession(mMediaSession.sessionToken)

            val notificationBuilder: NotificationCompat.Builder = NotificationCompat.Builder(this@MyService, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_music_note_black)
                .setLargeIcon(imageSinger)
                .setContentIntent(pendingIntent)
                .setContentTitle(song.title)
                .setShowWhen(false)
                .setContentText(song.singer[0].singername)
                .setStyle(style)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            if (isPlaying){
                notificationBuilder
                    .addAction(R.drawable.ic_previous,"Previous", getPendingIntent(this@MyService, ACTION_PREVIOUS))
                    .addAction(R.drawable.ic_baseline_pause_24,"PlayOrPause", getPendingIntent(this@MyService, ACTION_PAUSE))
                    .addAction(R.drawable.ic_next,"Next", getPendingIntent(this@MyService, ACTION_NEXT))
            }else{
                notificationBuilder
                    .addAction(R.drawable.ic_previous,"Previous", getPendingIntent(this@MyService, ACTION_PREVIOUS))
                    .addAction(R.drawable.ic_baseline_play_arrow_24,"PlayOrPause", getPendingIntent(this@MyService, ACTION_RESUME))
                    .addAction(R.drawable.ic_next,"Next", getPendingIntent(this@MyService, ACTION_NEXT))
            }

            val notification = notificationBuilder.build()

            startForeground(1, notification)
        }
    }

    //endregion
    //====================================================
    //region GỬI DỮ LIỆU ĐẾN ACTIVITY
    @SuppressLint("UnspecifiedImmutableFlag")
    private fun getPendingIntent(context: Context, action: Int): PendingIntent? {
        val intent = Intent(this, MyReceiver::class.java)
        intent.putExtra("action_music", action)

        return getBroadcast(context.applicationContext, action, intent, FLAG_UPDATE_CURRENT)
    }

    // gửi dữ liệu đến activity
    private fun sendActionToActivity(action: Int){
        val intent = Intent("send_action_to_activity")
        val bundle = Bundle()

        bundle.putSerializable("list_song", mList)
        bundle.putInt("position_song", mPosition)
        bundle.putBoolean("status_player", isPlaying)
        bundle.putInt("action_music", action)

        // lưu dữ liệu vào SharePreference
        MyDataLocalManager.setPlaylist(mList)
        MyDataLocalManager.setSong(mList[mPosition])
        MyDataLocalManager.setIsPlaying(isPlaying)
        MyDataLocalManager.setIsRepeat(isRepeat)
        MyDataLocalManager.setIsShuffle(isShuffle)
        intent.putExtras(bundle)

        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
    //endregion
    //====================================================


}