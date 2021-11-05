package com.example.appnghenhaconline.activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.core.view.isVisible
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.models.song.Song
import com.example.appnghenhaconline.models.user.User
import com.example.appnghenhaconline.service.MyService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_play_music.*
import kotlin.math.abs


class PlayMusicActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    private lateinit var gestureDetector : GestureDetector
    lateinit var mSong: Song
    var isPlaying: Boolean = false

    lateinit var btnPlayOrPause : ImageView
    lateinit var imgPlay : ImageView
    lateinit var imgCardViewPlay : CardView
    lateinit var imgBgPlay : ImageView
    lateinit var tvSongPlay : TextView

    companion object{
        const val MIN_DISTANCE = 100
        const val MIN_VELOCITY = 100
    }

    private var broadcastReceiver = object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val bundle: Bundle? = intent?.extras
            //nhận dữ liệu action từ MyService
            mSong = bundle?.get("item_song") as Song
            isPlaying = bundle.getBoolean("status_player")
            val actionMusic: Int = bundle.getInt("action_music")

            handleLayoutMusic(actionMusic)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_music)
        init()
        MyLib.hideSystemUI(window, layoutPlayMusic)
        swipeActivity()
        LocalBroadcastManager.getInstance(this)
                                .registerReceiver(broadcastReceiver,
                                IntentFilter("send_action_to_activity"))
    }
    private fun init(){
        btnPlayOrPause = findViewById(R.id.btnPlay_Pause)
        imgPlay = findViewById(R.id.imgSongPlayMusic)
        imgBgPlay = findViewById(R.id.imgBackgroundPlayMusic)
        tvSongPlay = findViewById(R.id.tvSongNamePlayMusic)
        imgCardViewPlay = findViewById(R.id.imgPlayMusic)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slilde_in_down, R.anim.slilde_out_down)
    }

    @SuppressLint("ClickableViewAccessibility")
    fun swipeActivity(){
        gestureDetector = GestureDetector(this, this)
        layoutPlayMusic.setOnTouchListener { v, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    private fun handleLayoutMusic(action: Int) {
        when(action){
            MyService.ACTION_PAUSE->{
                showInfoSong()
                setStatusButtonPlayOrPause()
            }
            MyService.ACTION_RESUME->{
                showInfoSong()
                setStatusButtonPlayOrPause()
            }
            MyService.ACTION_START->{
                showInfoSong()
                setStatusButtonPlayOrPause()
            }
            MyService.ACTION_INFO->{
                showInfoSong()
                setStatusButtonPlayOrPause()
            }
        }
    }

    private fun setStatusButtonPlayOrPause(){
        if (isPlaying){
            btnPlayOrPause.setImageResource(R.drawable.ic_baseline_pause_24)
        }else{
            btnPlayOrPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        }
    }

    private fun showInfoSong(){
        if (mSong==null) return

        Picasso.get().load(mSong.image)
//                        .resize(450,400)
                        .into(imgPlay)
        Picasso.get().load(mSong.image)
                        .resize(480,480)
                        .into(imgBgPlay)
        tvSongPlay.text = mSong.title

        btnPlayOrPause.setOnClickListener {
            if (isPlaying){
                sendActionToService(MyService.ACTION_PAUSE)
                val animZoomOut = AnimationUtils.loadAnimation(this, R.anim.anim_zoom_out_img)
                imgCardViewPlay.startAnimation(animZoomOut)
            }else{
                sendActionToService(MyService.ACTION_RESUME)
                val animZoomIn = AnimationUtils.loadAnimation(this, R.anim.anim_zoom_in_img)
                imgCardViewPlay.startAnimation(animZoomIn)
            }
        }
    }

    private fun sendActionToService(action: Int){
        val intent = Intent(this, MyService::class.java)
        intent.putExtra("action_music_service", action)

        startService(intent)
    }

    override fun onDown(e: MotionEvent?): Boolean {
        return false
    }

    override fun onShowPress(e: MotionEvent?) {

    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        return false
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?,
                          distanceX: Float, distanceY: Float): Boolean {
        return false
    }

    override fun onLongPress(e: MotionEvent?) {

    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?,
                         velocityX: Float, velocityY: Float): Boolean {
        if ( e1!!.y - e2!!.y < MIN_DISTANCE && abs(velocityY) > MIN_VELOCITY){
            MyLib.showToast(this,"Bottom")
            onBackPressed()
        }
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }
}