package com.example.appnghenhaconline.activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.animation.AnimationUtils
import androidx.cardview.widget.CardView
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.activity.HomeActivity.Companion.songObj
import com.example.appnghenhaconline.dataLocalManager.MyDataLocalManager
import com.example.appnghenhaconline.models.song.Song
import com.example.appnghenhaconline.dataLocalManager.Service.MyService
import com.example.appnghenhaconline.models.singer.Singer
import com.squareup.picasso.Picasso
import io.alterac.blurkit.BlurLayout
import kotlinx.android.synthetic.main.activity_play_music.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import kotlin.math.abs
import eightbitlab.com.blurview.RenderScriptBlur

import android.graphics.drawable.Drawable

import android.view.ViewGroup
import android.view.animation.LinearInterpolator
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.palette.graphics.Palette
import com.example.appnghenhaconline.fragment.library.LibraryFragment
import com.example.appnghenhaconline.fragment.playNow.PlayNowFragment
import com.example.appnghenhaconline.fragment.search.SearchFragment
import com.example.lib.LyricEntry
import com.example.lib.LyricUtil
import com.example.lib.LyricViewX
import com.example.lib.OnPlayClickListener
import com.squareup.picasso.Callback
import eightbitlab.com.blurview.BlurView
import java.lang.Exception
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_home.*
import java.io.File
import kotlin.math.log


class PlayMusicActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    private lateinit var gestureDetector : GestureDetector
    private lateinit var btnNext : ImageView
    private lateinit var btnPrev : ImageView
    private lateinit var imgCardViewPlay : CardView
    private lateinit var btnPlayOrPause : ImageView
    private lateinit var layoutImage : RelativeLayout
    private lateinit var tvNameSinger: TextView
    private lateinit var imgBgPlay: ImageView
    private lateinit var mainLayout: RelativeLayout
    private lateinit var blurLayout: BlurView
    private var mList: ArrayList<Song> = ArrayList()
    private lateinit var tvSongPlay : TextView
    private lateinit var startSeekBar :TextView
    private lateinit var seekBarMusic: SeekBar
    private lateinit var imgPlay : ImageView
    private lateinit var endSeekBar :TextView
    lateinit var btnRepeat : ImageView
    lateinit var btnShuffle : ImageView
    lateinit var btnLyric : ImageView
//    lateinit var layoutImg : ConstraintLayout

    var isPlaying: Boolean = false

    val mHandler = Handler()
    var mPosition: Int = 0
    lateinit var lyricView : LyricViewX


    companion object{
        const val MIN_DISTANCE = 100
        const val MIN_VELOCITY = 100
        var isShuffle: Boolean = false
        var isRepeat: Boolean = false
        var isLyric: Boolean = false
    }

    //===============================================
    //region broadcastReceiver
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
    //endregion
    //===============================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_play_music)
        init()
    }

    override fun onStart() {
        super.onStart()
        initSongInfo()
        customBlurView()
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(broadcastReceiver, IntentFilter("send_action_to_activity"))

    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slilde_in_down, R.anim.slilde_out_down)
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        mHandler.removeCallbacks(runnable)
        mHandler.removeCallbacks(runnableAnim)
        super.onDestroy()
    }

    //===============================================
    //region INIT
    private fun init(){
//        layoutImg = findViewById(R.id.layoutImg)
        layoutImage = findViewById(R.id.layoutRelative)
        MyLib.hideSystemUI(window, layoutPlayMusic)
        btnPlayOrPause = findViewById(R.id.btnPlay_Pause)
        imgPlay = findViewById(R.id.imgSongPlayMusic)
        imgBgPlay = findViewById(R.id.imgBackgroundPlayMusic)
        mainLayout = findViewById(R.id.background)
        tvSongPlay = findViewById(R.id.tvSongNamePlayMusic)
        imgCardViewPlay = findViewById(R.id.imgPlayMusic)
        blurLayout = findViewById(R.id.blurLayout)
        btnNext = findViewById(R.id.btnNextPlay)
        btnPrev = findViewById(R.id.btnPrevPlay)
        startSeekBar = findViewById(R.id.tvStartSeekbar)
        endSeekBar = findViewById(R.id.tvEndSeekbar)
        seekBarMusic = findViewById(R.id.seekbarPlay)
        tvNameSinger = findViewById(R.id.tvAuthorPlayMusic)
        btnRepeat = findViewById(R.id.btnRepeat)
        btnShuffle = findViewById(R.id.btnShuffle)
        btnLyric = findViewById(R.id.btnLyric)
        lyricView = findViewById(R.id.lyricView)
        swipeActivity()


    }
    //endregion
    //===============================================
    //region SERVICE
    //set hành động sau khi nhận từ service
    private fun handleLayoutMusic(action: Int) {
        when(action){
            MyService.ACTION_PAUSE->{
//                initSongInfo()

                setStatusButtonPlayOrPause()
            }
            MyService.ACTION_RESUME->{
//                initSongInfo()

                setStatusButtonPlayOrPause()
            }
            MyService.ACTION_START->{
                initSongInfo()
                setStatusButtonPlayOrPause()
            }
            MyService.ACTION_NEXT->{
                initSongInfo()
//                setStatusButtonPlayOrPause()
            }
            MyService.ACTION_PREVIOUS->{
//                setStatusButtonPlayOrPause()
                initSongInfo()
            }
            MyService.ACTION_LYRIC->{
//                initSongInfo()
            }
        }
    }

    //set trạng thái button play
    private fun setStatusButtonPlayOrPause(){
        if (isPlaying){
            btnPlayOrPause.setImageResource(R.drawable.ic_pause_circle_white)
        }else{
            btnPlayOrPause.setImageResource(R.drawable.ic_play_circle_white)
        }
    }

    //gửi hành động tới service
    private fun sendActionToService(action: Int){
        val intent = Intent(this, MyService::class.java)
        intent.putExtra("action_music_service", action)
        startService(intent)
    }
    //endregion
    //===============================================
    //region SỰ KIỆN VUỐT MÀN HÌNH
    @SuppressLint("ClickableViewAccessibility")
    fun swipeActivity(){
        gestureDetector = GestureDetector(this, this)
        layoutPlayMusic.setOnTouchListener { v, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }
    @SuppressLint("ClickableViewAccessibility")

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
            onBackPressed()
        }
        return false
    }
    //endregion
    //===============================================
    //region ANOTHER FUNC
    private fun customBlurView(){
        val radius = 20f

        val decorView = window.decorView
        val rootView = decorView.findViewById<View>(android.R.id.content) as ViewGroup
        val windowBackground = decorView.background

        blurLayout.setupWith(rootView)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(this))
            .setBlurRadius(radius)
            .setBlurAutoUpdate(true)
            .setHasFixedTransformationMatrix(false) // Or false if it's in a scrolling container or might be animated

        val animationDrawable: AnimationDrawable = mainLayout.background as AnimationDrawable
        animationDrawable.setEnterFadeDuration(2500)
        animationDrawable.setExitFadeDuration(5000)
        animationDrawable.start()

        tvSongPlay.isSelected = true
    }
    //set chế độ lặp lại
    private fun repeatSong(){
        if (!isRepeat){
            isRepeat = true
            btnRepeat.setImageResource(R.drawable.ic_repeat)
        }else{
            isRepeat = false
            btnRepeat.setImageResource(R.drawable.ic_repeat_white)
        }
        MyDataLocalManager.setIsRepeat(isRepeat)
    }

    //set chế độ random
    private fun shuffleSong(){
        if (!isShuffle){
            isShuffle = true
            btnShuffle.setImageResource(R.drawable.ic_shuffle)
        }else{
            isShuffle = false
            btnShuffle.setImageResource(R.drawable.ic_shuffle_white)
        }
        MyDataLocalManager.setIsShuffle(isShuffle)
    }

    //xem lời bài hát
    private fun lyricSong(){
        if (!isLyric ){
            isLyric = true
            btnLyric.setImageResource(R.drawable.ic_lyrics)
            layoutImage.clearAnimation()
            layoutImage.visibility = View.INVISIBLE
            lyricView.visibility = View.VISIBLE

        }else{
            isLyric = false
            btnLyric.setImageResource(R.drawable.ic_lyrics_white)
            layoutImage.clearAnimation()
            layoutImage.visibility = View.VISIBLE
            lyricView.visibility = View.INVISIBLE
        }
        MyDataLocalManager.setIsLyric(isLyric)
        sendActionToService(MyService.ACTION_LYRIC)
    }

    private fun loadLyric(lyric : String){
        if (isLyric){
            lyricView.setLabel("Hiện chưa có lời bài hát")
            lyricView.setCurrentTextSize(65f)
            lyricView.setNormalTextSize(60f)

            lyric.trimIndent()
            lyricView.loadLyric(lyric)

            lyricView.setDraggable(true, object : OnPlayClickListener {
                override fun onPlayClick(time: Long): Boolean {
                    lyricView.updateTime(time)
                    var timeSync = time.toInt()
                    MyService.mediaPlayer.seekTo(timeSync)
                    seekBarMusic.progress = timeSync
                    return true
                }
            })
        }
    }
    //chỗ này là lyrics



    //runtime set thanh process bar
    private var runnable = object: Runnable{
        @SuppressLint("SimpleDateFormat")
        override fun run() {
            val time = SimpleDateFormat("mm:ss")
            endSeekBar.text = time.format(MyService.mediaPlayer.duration - MyService.mediaPlayer.currentPosition)
            startSeekBar.text = time.format(MyService.mediaPlayer.currentPosition)
            seekBarMusic.progress = MyService.mediaPlayer.currentPosition

            var lyricTime = seekBarMusic.progress.toLong()
            lyricView.updateTime(lyricTime)

            MyService.mediaPlayer.setOnCompletionListener {
                if (isRepeat){
                    sendActionToService(MyService.ACTION_REPEAT)
                }else{
                    sendActionToService(MyService.ACTION_NEXT)
                }
            }

            mHandler.postDelayed(this, 1000)
        }
    }

    private var runnableAnim = object: Runnable{
        override fun run() {
            mainLayout.animate().rotationBy(360f).withEndAction(this)
                .setDuration(50000)
                .setInterpolator(LinearInterpolator())
                .start()
        mHandler.postDelayed(this, 50000)
        }
    }

    //set thông tin bài hát

    private fun initSongInfo(){
        val lyric = songObj.lyric
        tvSongPlay.text = songObj.title
        tvNameSinger.text = songObj.singer[0].singername
        seekBarMusic.max = MyService.mediaPlayer.duration
        loadLyric(lyric.toString())
//        lyricView.setLabel("Hiện chưa có lời bài hát")
//        lyricView.setCurrentTextSize(65f)
//        lyricView.setNormalTextSize(60f)
//        lyric?.trimIndent()
//        lyricView.loadLyric(lyric)
//        lyricView.setDraggable(true, object : OnPlayClickListener {
//            override fun onPlayClick(time: Long): Boolean {
//                lyricView.updateTime(time)
//                var timeSync = time.toInt()
//                MyService.mediaPlayer.seekTo(timeSync)
//                seekBarMusic.progress = timeSync
//                return true
//            }
//        })
//        MyLib.showToast(this,lyric)

        val isPlayingData: Boolean = MyDataLocalManager.getIsPlaying()
        val isRepeatData: Boolean = MyDataLocalManager.getIsRepeat()
        val isShuffleData: Boolean = MyDataLocalManager.getIsShuffle()
        val isLyricData: Boolean = MyDataLocalManager.getIsLyric();


        val scope = CoroutineScope(Job()+ Dispatchers.Main)
        scope.launch {
            Picasso.get().load(songObj.image)
                .into(imgPlay, object : Callback{
                    override fun onSuccess() {
                        val bitmap: Bitmap = (imgPlay.drawable as BitmapDrawable).bitmap
                        Palette.from(bitmap).generate {palette->
                            val mutedDarkColor: Palette.Swatch? = palette?.darkMutedSwatch
                            val mutedColor: Palette.Swatch? = palette?.mutedSwatch

                            if (mutedDarkColor != null) {
                                imgBgPlay.setBackgroundColor(mutedDarkColor.rgb)
                            }else if (mutedColor != null){
                                imgBgPlay.setBackgroundColor(mutedColor.rgb)
                            }
                        }
                    }

                    override fun onError(e: Exception?) {
                        val snackbar = Snackbar
                            .make(layoutPlayMusic, "Error on image load.", Snackbar.LENGTH_LONG)
                        snackbar.show()
                    }
                })
        }

        runnable.run()
        runnableAnim.run()

        val animZoomIn = AnimationUtils.loadAnimation(this, R.anim.anim_zoom_in_img)
        val animZoomOut = AnimationUtils.loadAnimation(this, R.anim.anim_zoom_out_img)

        if (isPlayingData && !isLyricData){
            layoutImage.startAnimation(animZoomIn)
            btnPlayOrPause.setImageResource(R.drawable.ic_pause_circle_white)
            btnLyric.setImageResource(R.drawable.ic_lyrics_white)

        }else if (!isPlayingData && !isLyricData){
            layoutImage.startAnimation(animZoomOut)
            btnPlayOrPause.setImageResource(R.drawable.ic_play_circle_white)
            btnLyric.setImageResource(R.drawable.ic_lyrics_white)

        }else if (isPlayingData && isLyricData){
            layoutImage.clearAnimation()
            layoutImage.visibility = View.INVISIBLE
            lyricView.visibility = View.VISIBLE
            btnPlayOrPause.setImageResource(R.drawable.ic_pause_circle_white)
            btnLyric.setImageResource(R.drawable.ic_lyrics)

        }else{
            layoutImage.clearAnimation()
            layoutImage.visibility = View.INVISIBLE
            lyricView.visibility = View.VISIBLE
            btnPlayOrPause.setImageResource(R.drawable.ic_play_circle_white)
            btnLyric.setImageResource(R.drawable.ic_lyrics)
        }

        if (isRepeatData){
            btnRepeat.setImageResource(R.drawable.ic_repeat)
        }else{
            btnRepeat.setImageResource(R.drawable.ic_repeat_white)
        }

        if (isShuffleData){
            btnShuffle.setImageResource(R.drawable.ic_shuffle)
        }else{
            btnShuffle.setImageResource(R.drawable.ic_shuffle_white)
        }

        btnPlayOrPause.setOnClickListener {
            if (isPlayingData && isLyricData){
                sendActionToService(MyService.ACTION_PAUSE)
            }else if (!isPlayingData && isLyricData){
                sendActionToService(MyService.ACTION_RESUME)
            }else if (isPlayingData && !isLyricData){
                sendActionToService(MyService.ACTION_PAUSE)
                layoutImage.startAnimation(animZoomOut)
            }else{
                sendActionToService(MyService.ACTION_RESUME)
                layoutImage.startAnimation(animZoomIn)
            }
        }

        btnNext.setOnClickListener {
            sendActionToService(MyService.ACTION_NEXT)
        }

        btnPrev.setOnClickListener {
            sendActionToService(MyService.ACTION_PREVIOUS)
        }

        btnRepeat.setOnClickListener {
            repeatSong()
        }

        btnShuffle.setOnClickListener {
            shuffleSong()
        }
        btnLyric.setOnClickListener {
            lyricSong()
            if (lyric != null) {
                loadLyric(lyric)
            }
        }


        seekBarMusic.setOnSeekBarChangeListener(object: SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
            }


            override fun onStartTrackingTouch(seekBar: SeekBar?) {
                MyService.mediaPlayer.seekTo(seekBarMusic.progress)
            }
            //
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
                MyService.mediaPlayer.seekTo(seekBarMusic.progress)
            }
        })
    }
    //endregion
    //===============================================
}