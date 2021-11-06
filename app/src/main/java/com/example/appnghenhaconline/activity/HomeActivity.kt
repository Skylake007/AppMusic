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
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.fragment.*
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.models.song.Song
import com.example.appnghenhaconline.models.user.User
import com.example.appnghenhaconline.service.MyService
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import kotlin.math.abs

class HomeActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    private lateinit var gestureDetector : GestureDetector
    private lateinit var menuUser: ImageView
    lateinit var btnPlayOrPause : ImageView
    lateinit var imgPlayNav : ImageView
    lateinit var tvPlayNav : TextView
    lateinit var mSong: Song
    var isPlaying: Boolean = false

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

    companion object{
        const val MIN_DISTANCE = 100
        const val MIN_VELOCITY = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        MyLib.hideSystemUI(window, layoutHomeActivity)
        init()
        initMenu()
        val user = intent.getSerializableExtra("User") as? User
        Log.e(null, user?.id!!)
        LocalBroadcastManager.getInstance(this)
                                .registerReceiver(broadcastReceiver,
                                IntentFilter("send_action_to_activity"))
    }

    private fun init(){
        btnPlayOrPause = findViewById(R.id.btnPlayOrPause)
        imgPlayNav = findViewById(R.id.imgPlayNav)
        tvPlayNav = findViewById(R.id.tvPlayNav)
        menuUser = findViewById(R.id.setting)
    }

    private fun initMenu(){
        //mặc định load trang là PlayNowFragment
        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, PlayNowFragment()).apply {
                                tvFragment.setText(R.string.action_play_now)
                                imgTopNav.setImageResource(R.drawable.ic_play_circle)
                             }.commit()
        //set sự kiện chuyển fragment
        bottomNav.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment = PlayNowFragment()
            when (item.itemId) {
                R.id.action_play_now -> {
                    selectedFragment = PlayNowFragment()
                    tvFragment.setText(R.string.action_play_now)
                    imgTopNav.setImageResource(R.drawable.ic_play_circle)
                }

                R.id.action_radio -> {
                    selectedFragment = LibraryFragment()
                    tvFragment.setText(R.string.action_library)
                    imgTopNav.setImageResource(R.drawable.ic_library)
                }
                R.id.action_search -> {
                    selectedFragment = SearchFragment()
                    tvFragment.setText(R.string.action_search)
                    imgTopNav.setImageResource(R.drawable.ic_search)
                }
            }
            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainer, selectedFragment)
                                .commit()
            true

        }
        //khởi tạo menu dropdown
        menuUser.setOnClickListener {v ->
            intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }
        //khởi tạo sự kiện click và vuốt cho PlayNav
        showPlayMusicFragment()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun showPlayMusicFragment(){
        gestureDetector = GestureDetector(this, this)
        layoutInfo.setOnTouchListener { v, event ->
            gestureDetector.onTouchEvent(event)
            true
        }
    }

    override fun onFling(e1: MotionEvent?, e2: MotionEvent?,
                         velocityX: Float, velocityY: Float): Boolean {
        if ( e1!!.y - e2!!.y > MIN_DISTANCE && abs(velocityY) > MIN_VELOCITY){
            val intent = Intent(this, PlayMusicActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slilde_in_up, R.anim.slilde_out_up)
        }
        return false
    }

    override fun onDown(e: MotionEvent?): Boolean {
//        TODO("Not yet implemented")
        return false
    }

    override fun onShowPress(e: MotionEvent?) {
//        TODO("Not yet implemented")
    }

    override fun onSingleTapUp(e: MotionEvent?): Boolean {
        val intent = Intent(this, PlayMusicActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slilde_in_up, R.anim.slilde_out_up)
        return true
    }

    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?,
                          distanceX: Float, distanceY: Float): Boolean {
//        TODO("Not yet implemented")
        return false
    }

    override fun onLongPress(e: MotionEvent?) {
        TODO("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
    }
    //set sự kiện khi nhận action từ service
    private fun handleLayoutMusic(action: Int) {
        when(action){
            MyService.ACTION_PAUSE->{
                setStatusButtonPlayOrPause()
            }
            MyService.ACTION_RESUME->{
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

    private fun showInfoSong(){
        Picasso.get().load(mSong.image)
                        .into(imgPlayNav)
        tvPlayNav.text = mSong.title

        btnPlayOrPause.setOnClickListener {
            if (isPlaying){
                sendActionToService(MyService.ACTION_PAUSE)
            }else{
                sendActionToService(MyService.ACTION_RESUME)
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

    private fun sendActionToService(action: Int){
        val intent = Intent(this, MyService::class.java)
        intent.putExtra("action_music_service", action)

        startService(intent)
    }
}