package com.example.appnghenhaconline.fragment.playMusic

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.os.Handler
import android.view.GestureDetector
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.palette.graphics.Palette
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.activity.HomeActivity
import com.example.appnghenhaconline.activity.PlayMusicActivity
import com.example.appnghenhaconline.dataLocalManager.MyDataLocalManager
import com.example.appnghenhaconline.dataLocalManager.Service.MyService
import com.example.appnghenhaconline.models.song.Song
import com.example.lib.LyricViewX
import com.example.lib.OnPlayClickListener
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import eightbitlab.com.blurview.BlurView
import eightbitlab.com.blurview.RenderScriptBlur
import kotlinx.android.synthetic.main.activity_play_music.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.lang.Exception
import java.text.SimpleDateFormat

class TabPlayMusic: Fragment() {
    internal lateinit var view: View
    private lateinit var btnNext : ImageView
    private lateinit var btnPrev : ImageView
    private lateinit var imgCardViewPlay : CardView
    private lateinit var btnPlayOrPause : ImageView
    private lateinit var tvNameSinger: TextView
    private lateinit var imgBgPlay: ImageView
    private lateinit var mainLayout: RelativeLayout
    private lateinit var blurLayout: BlurView
    private var mList: ArrayList<Song> = ArrayList()
    private lateinit var tvSongPlay : TextView
    private lateinit var startSeekBar : TextView
    private lateinit var seekBarMusic: SeekBar
    private lateinit var imgPlay : ImageView
    private lateinit var endSeekBar : TextView
    lateinit var btnRepeat : ImageView
    lateinit var btnShuffle : ImageView
    lateinit var btnLyric : ImageView
    lateinit var layoutImg : ConstraintLayout

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
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                            savedInstanceState: Bundle?): View? {

        view = inflater.inflate(R.layout.tab_play_music, container, false)
        init()
        return view
    }

    override fun onStart() {
        super.onStart()
        initSongInfo()
        customBlurView()
        LocalBroadcastManager.getInstance(requireContext())
            .registerReceiver(broadcastReceiver, IntentFilter("send_action_to_activity"))
    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(broadcastReceiver)
        mHandler.removeCallbacks(runnable)
        mHandler.removeCallbacks(runnableAnim)
        super.onDestroy()
    }

    //===============================================
    //region INIT
    private fun init(){
        layoutImg = view.findViewById(R.id.layoutImg)
        btnPlayOrPause = view.findViewById(R.id.btnPlay_Pause)
        imgPlay = view.findViewById(R.id.imgSongPlayMusic)
        imgBgPlay = view.findViewById(R.id.imgBackgroundPlayMusic)
        mainLayout = view.findViewById(R.id.background)
        tvSongPlay = view.findViewById(R.id.tvSongNamePlayMusic)
        imgCardViewPlay = view.findViewById(R.id.imgPlayMusic)
        blurLayout = view.findViewById(R.id.blurLayout)
        btnNext = view.findViewById(R.id.btnNextPlay)
        btnPrev = view.findViewById(R.id.btnPrevPlay)
        startSeekBar = view.findViewById(R.id.tvStartSeekbar)
        endSeekBar = view.findViewById(R.id.tvEndSeekbar)
        seekBarMusic = view.findViewById(R.id.seekbarPlay)
        tvNameSinger = view.findViewById(R.id.tvAuthorPlayMusic)
        btnRepeat = view.findViewById(R.id.btnRepeat)
        btnShuffle = view.findViewById(R.id.btnShuffle)
        btnLyric = view.findViewById(R.id.btnLyric)
        lyricView = view.findViewById(R.id.lyricVieew)
    }
    //endregion
    //===============================================
    //region SERVICE
    //set hành động sau khi nhận từ service
    private fun handleLayoutMusic(action: Int) {
        when(action){
            MyService.ACTION_PAUSE->{
                initSongInfo()
                setStatusButtonPlayOrPause()
            }
            MyService.ACTION_RESUME->{
                initSongInfo()
                setStatusButtonPlayOrPause()
            }
            MyService.ACTION_START->{
                initSongInfo()
                setStatusButtonPlayOrPause()
            }
            MyService.ACTION_NEXT->{
                initSongInfo()
            }
            MyService.ACTION_PREVIOUS->{
                initSongInfo()
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
        val intent = Intent(requireContext(), MyService::class.java)
        intent.putExtra("action_music_service", action)
        requireActivity().startService(intent)
    }
    //endregion
    //===============================================
    //region ANOTHER FUNC
    private fun customBlurView(){
        val radius = 20f

        val decorView = activity?.window?.decorView
        val rootView = decorView?.findViewById<View>(android.R.id.content) as ViewGroup
        val windowBackground = decorView.background

        blurLayout.setupWith(rootView)
            .setFrameClearDrawable(windowBackground)
            .setBlurAlgorithm(RenderScriptBlur(requireContext()))
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
        if (!PlayMusicActivity.isRepeat){
            PlayMusicActivity.isRepeat = true
            btnRepeat.setImageResource(R.drawable.ic_repeat)
        }else{
            PlayMusicActivity.isRepeat = false
            btnRepeat.setImageResource(R.drawable.ic_repeat_white)
        }
        MyDataLocalManager.setIsRepeat(PlayMusicActivity.isRepeat)
    }

    //set chế độ random
    private fun shuffleSong(){
        if (!PlayMusicActivity.isShuffle){
            PlayMusicActivity.isShuffle = true
            btnShuffle.setImageResource(R.drawable.ic_shuffle)
        }else{
            PlayMusicActivity.isShuffle = false
            btnShuffle.setImageResource(R.drawable.ic_shuffle_white)
        }
        MyDataLocalManager.setIsShuffle(PlayMusicActivity.isShuffle)
    }
    //xem lời bài hát
    //mainLyricFile: File, secondLyricFile: File? = null
    private fun loadLyric(){
        if (!PlayMusicActivity.isLyric){
            PlayMusicActivity.isLyric = true
            btnLyric.setImageResource(R.drawable.ic_lyrics)
            imgPlay.visibility = View.GONE
            lyricView.visibility = View.VISIBLE

            val lyric = """
            [00:05.70] Đây là lyric 1
            [00:06.45] Đây là lyric 2
            [00:07.52] Đây là lyric 3
            [00:09.02] Đây là lyric 4
            [00:23.97] Đây là lyric 5
            [00:33.61] Đây là lyric 6
            [00:38.00] Đây là lyric 7
            [00:41.56] Đây là lyric 8
            [00:44.40] Đây là lyric 9
            [00:50.66] Đây là lyric 10
            [00:52.97] Đây là lyric 11
            [00:59.14] 往海的深处听
            [01:03.45] 谁的哀鸣在指引
            [01:08.65] 灵魂没入寂静
            [01:10.94] 无人将你吵醒
            [01:17.23] 你喜欢海风咸咸的气息
            [01:20.10] 踩着湿湿的沙砾
            [01:22.37] 你说人们的骨灰应该撒进海里
            [01:26.09] 你问我死后会去哪里
            [01:29.03] 有没有人爱你
            [01:31.09] 世界能否不再
            [01:35.27] 总爱对凉薄的人扯着笑脸
            [01:39.70] 岸上人们脸上都挂着无关
            [01:44.20] 人间毫无留恋
            [01:46.51] 一切散为烟
            [02:24.45] 散落的月光穿过了云
            [02:33.65] 躲着人群
            [02:38.07] 溜进海底
            [02:42.03] 海浪清洗血迹
            [02:44.64] 妄想温暖你
            [02:50.80] 灵魂没入寂静
            [02:53.27] 无人将你吵醒
            [02:59.60] 你喜欢海风咸咸的气息
            [03:02.25] 踩着湿湿的沙砾
            [03:04.60] 你说人们的骨灰应该撒进海里
            [03:08.16] 你问我死后会去哪里
            [03:11.07] 有没有人爱你
            [03:13.29] 世界已然将你抛弃
            [03:17.49] 总爱对凉薄的人扯着笑脸
            [03:21.90] 岸上人们脸上都挂着无关
            [03:26.32] 人间毫无留恋
            [03:28.53] 一切散为烟
            [03:34.70] 来不及来不及
            [03:39.30] 你曾笑着哭泣
            [03:43.47] 来不及来不及
            [03:47.75] 你颤抖的手臂
            [03:52.07] 来不及来不及
            [03:56.69] 无人将你打捞起
            [04:01.39] 来不及来不及
            [04:05.77] 你明明讨厌窒息
            """.trimIndent()
            lyricView.loadLyric(lyric)
            lyricView.setDraggable(true, object : OnPlayClickListener {
                override fun onPlayClick(time: Long): Boolean {
                    lyricView.updateTime(time)
                    MyLib.showLog(time.toString())

                    //seekBarMusic.progress = time.toInt()
                    MyService.mediaPlayer.seekTo(time.toInt())

                    seekBarMusic.progress = MyService.mediaPlayer.currentPosition
                    return true

                }
            })

        }else{
            PlayMusicActivity.isLyric = false
            btnLyric.setImageResource(R.drawable.ic_lyrics_white)
            imgPlay.visibility = View.VISIBLE
            lyricView.visibility = View.GONE
        }
        //MyDataLocalManager.setIsLyric(isLyrics)
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


            MyService.mediaPlayer.setOnCompletionListener {
                if (PlayMusicActivity.isRepeat){
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
    private fun initSongInfo() {
        tvSongPlay.text = HomeActivity.songObj.title
        tvNameSinger.text = HomeActivity.songObj.singer[0].singername

        seekBarMusic.max = MyService.mediaPlayer.duration

        val isPlayingData: Boolean = MyDataLocalManager.getIsPlaying()
        val isRepeatData: Boolean = MyDataLocalManager.getIsRepeat()
        val isShuffleData: Boolean = MyDataLocalManager.getIsShuffle()
        val isLyricData: Boolean = false;

        runnable.run()
        val scope = CoroutineScope(Job() + Dispatchers.Main)
        scope.launch {
            Picasso.get().load(HomeActivity.songObj.image)
                .into(imgPlay, object : Callback {
                    override fun onSuccess() {
                        val bitmap: Bitmap = (imgPlay.drawable as BitmapDrawable).bitmap
                        Palette.from(bitmap).generate { palette->
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

        runnableAnim.run()

        val animZoomIn = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_zoom_in_img)
        val animZoomOut = AnimationUtils.loadAnimation(requireContext(), R.anim.anim_zoom_out_img)

        if (isPlayingData){
            imgCardViewPlay.startAnimation(animZoomIn)
            btnPlayOrPause.setImageResource(R.drawable.ic_pause_circle_white)
        }else{
            imgCardViewPlay.startAnimation(animZoomOut)
            btnPlayOrPause.setImageResource(R.drawable.ic_play_circle_white)
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

        if (isLyricData){
            btnLyric.setImageResource(R.drawable.ic_lyrics)
        }else{
            btnLyric.setImageResource(R.drawable.ic_lyrics_white)
        }

        btnPlayOrPause.setOnClickListener {
            if (isPlayingData){
                sendActionToService(MyService.ACTION_PAUSE)
                imgCardViewPlay.startAnimation(animZoomOut)
            }else{
                sendActionToService(MyService.ACTION_RESUME)
                imgCardViewPlay.startAnimation(animZoomIn)
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
            loadLyric()
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