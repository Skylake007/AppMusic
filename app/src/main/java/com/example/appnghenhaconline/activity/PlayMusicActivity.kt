package com.example.appnghenhaconline.activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.dataLocalManager.MyDataLocalManager
import com.example.appnghenhaconline.models.song.Song
import com.example.appnghenhaconline.models.user.User
import com.example.appnghenhaconline.dataLocalManager.Service.MyService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_play_music.*
import java.text.SimpleDateFormat
import kotlin.math.abs


class PlayMusicActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    private lateinit var gestureDetector : GestureDetector
    private lateinit var btnNext : ImageView
    private lateinit var btnPrev : ImageView
    lateinit var imgCardViewPlay : CardView
    lateinit var btnPlayOrPause : ImageView
    lateinit var imgBgPlay : ImageView
    lateinit var mList: ArrayList<Song>
    lateinit var tvSongPlay : TextView
    lateinit var startSeekBar :TextView
    lateinit var seekBarMusic: SeekBar
    lateinit var imgPlay : ImageView
    lateinit var endSeekBar :TextView
    var isPlaying: Boolean = false
    val mHandler = Handler()
    var mPosition: Int = 0

    companion object{
        const val MIN_DISTANCE = 100
        const val MIN_VELOCITY = 100
    }

    private var broadcastReceiver = object: BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            val bundle: Bundle? = intent?.extras
            //nhận dữ liệu action từ MyService
            mPosition = bundle?.get("position_song") as Int
            mList = bundle.get("list_song") as ArrayList<Song>
            isPlaying = bundle.getBoolean("status_player")

            val actionMusic: Int = bundle.getInt("action_music")

            handleLayoutMusic(actionMusic)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_music)
        init()
        swipeActivity()
        initSongInfo()
    }

    override fun onStart() {
        super.onStart()
        MyLib.hideSystemUI(window, layoutPlayMusic)

        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
            IntentFilter("send_action_to_activity"))
    }

    private fun init(){
        btnPlayOrPause = findViewById(R.id.btnPlay_Pause)
        imgPlay = findViewById(R.id.imgSongPlayMusic)
        imgBgPlay = findViewById(R.id.imgBackgroundPlayMusic)
        tvSongPlay = findViewById(R.id.tvSongNamePlayMusic)
        imgCardViewPlay = findViewById(R.id.imgPlayMusic)
        btnNext = findViewById(R.id.btnNextPlay)
        btnPrev = findViewById(R.id.btnPrevPlay)
        startSeekBar = findViewById(R.id.tvStartSeekbar)
        endSeekBar = findViewById(R.id.tvEndSeekbar)
        seekBarMusic = findViewById(R.id.seekbarPlay)
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
//                showInfoSong()
                initSongInfo()
                setStatusButtonPlayOrPause()
            }
            MyService.ACTION_RESUME->{
                initSongInfo()
//                showInfoSong()
                setStatusButtonPlayOrPause()
            }
            MyService.ACTION_START->{
                initSongInfo()
//                showInfoSong()
                setStatusButtonPlayOrPause()
            }
            MyService.ACTION_NEXT->{
                initSongInfo()
//                showInfoSong()
            }
            MyService.ACTION_PREVIOUS->{
                initSongInfo()
//                showInfoSong()
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
//        endSeekBar.text = totalSeekbar
        Picasso.get().load(mList[mPosition].image)
//                        .resize(450,400)
                        .into(imgPlay)
        Picasso.get().load(mList[mPosition].image)
                        .resize(480,480)
                        .into(imgBgPlay)
        tvSongPlay.text = mList[mPosition].title

        val animZoomIn = AnimationUtils.loadAnimation(this, R.anim.anim_zoom_in_img)
        val animZoomOut = AnimationUtils.loadAnimation(this, R.anim.anim_zoom_out_img)

        if (isPlaying){
            imgCardViewPlay.startAnimation(animZoomIn)
        }else{
            imgCardViewPlay.startAnimation(animZoomOut)
        }

        btnPlayOrPause.setOnClickListener {
            if (isPlaying){
                sendActionToService(MyService.ACTION_PAUSE)
                imgCardViewPlay.startAnimation(animZoomOut)
            }else{
                sendActionToService(MyService.ACTION_RESUME)
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

    private var runnable = object: Runnable{
        @SuppressLint("SimpleDateFormat")
        override fun run() {
            val time = SimpleDateFormat("mm:ss")
            endSeekBar.text = time.format(MyService.mediaPlayer.duration - MyService.mediaPlayer.currentPosition)
            startSeekBar.text = time.format(MyService.mediaPlayer.currentPosition)
            seekBarMusic.progress = MyService.mediaPlayer.currentPosition

            MyService.mediaPlayer.setOnCompletionListener {
                sendActionToService(MyService.ACTION_NEXT)
            }

            mHandler.postDelayed(this, 1000)
        }
    }

    private fun initSongInfo() {
        seekBarMusic.max = MyService.mediaPlayer.duration

        val songData: Song = MyDataLocalManager.getSong()
        val isPlayingData: Boolean = MyDataLocalManager.getIsPlaying()

        tvSongPlay.text = songData.title
        Picasso.get().load(songData.image)
                  .resize(450,550)
                    .into(imgPlay)
        Picasso.get().load(songData.image)
                    .resize(480,480)
                    .into(imgBgPlay)

        val animZoomIn = AnimationUtils.loadAnimation(this, R.anim.anim_zoom_in_img)
        val animZoomOut = AnimationUtils.loadAnimation(this, R.anim.anim_zoom_out_img)

        if (isPlayingData){
            imgCardViewPlay.startAnimation(animZoomIn)
            btnPlayOrPause.setImageResource(R.drawable.ic_baseline_pause_24)
        }else{
            imgCardViewPlay.startAnimation(animZoomOut)
            btnPlayOrPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
        }

        btnPlayOrPause.setOnClickListener {
            if (isPlayingData){
                sendActionToService(MyService.ACTION_PAUSE)
                imgCardViewPlay.startAnimation(animZoomOut)
                initSongInfo()
            }else{
                sendActionToService(MyService.ACTION_RESUME)
                imgCardViewPlay.startAnimation(animZoomIn)
            }
            runnable.run()
        }

        btnNext.setOnClickListener {
            sendActionToService(MyService.ACTION_NEXT)
            runnable.run()
        }

        btnPrev.setOnClickListener {
            sendActionToService(MyService.ACTION_PREVIOUS)
            runnable.run()
        }

        seekBarMusic.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
       //         TODO("Not yet implemented")
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {
//                TODO("Not yet implemented")
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                MyService.mediaPlayer.seekTo(seekBarMusic.progress)
            }
        })
    }
}