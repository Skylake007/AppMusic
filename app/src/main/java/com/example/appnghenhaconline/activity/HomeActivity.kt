package com.example.appnghenhaconline.activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
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
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_play_music.*
import kotlin.math.abs

class HomeActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    private lateinit var gestureDetector : GestureDetector
    lateinit var mediaPlayer : MediaPlayer
    lateinit var mSong: Song
    var isPlaying: Boolean = false
    lateinit var btnPlayOrPause : ImageView
    lateinit var imgPlayNav : ImageView
    lateinit var tvPlayNav : TextView

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

    companion object{
        const val MIN_DISTANCE = 100
        const val MIN_VELOCITY = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        btnPlayOrPause = findViewById(R.id.btnPlayOrPause)
        imgPlayNav = findViewById(R.id.imgPlayNav)
        tvPlayNav = findViewById(R.id.tvPlayNav)

        hideSystemUI()
        initBottomNav()
        showPlayMusicFragment()
        val user = intent.getSerializableExtra("User") as? User
        Log.e(null, user?.id!!)
//        eventMusic()
        LocalBroadcastManager.getInstance(this)
                                    .registerReceiver(broadcastReceiver,
                                    IntentFilter("send_action_to_activity"))
    }

    private fun initBottomNav(){
        //mặc định load trang là PlayNowFragment
        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragmentContainer, PlayNowFragment())
                            .apply {
                                tvFragment.text = "Nghe ngay"
                                imgTopNav.setImageResource(R.drawable.ic_play_circle)
                             }.commit()
        //click chuyển trang
        bottomNav.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment = PlayNowFragment()
            when (item.itemId) {
                R.id.action_play_now -> {
                    selectedFragment = PlayNowFragment()
                    tvFragment.text = "Nghe ngay"
                    imgTopNav.setImageResource(R.drawable.ic_play_circle)
                    Toast.makeText(this@HomeActivity, "Nghe ngay", Toast.LENGTH_SHORT).show()
                }
                R.id.action_discover -> {
                    selectedFragment = DiscoverFragment()
                    tvFragment.text = "Khám phá"
                    imgTopNav.setImageResource(R.drawable.ic_dashboard)
                    Toast.makeText(this@HomeActivity, "Khám phá", Toast.LENGTH_SHORT).show()
                }
                R.id.action_radio -> {
                    selectedFragment = RadioFragment()
                    tvFragment.text = "Radio"
                    imgTopNav.setImageResource(R.drawable.ic_podcasts)
                    Toast.makeText(this@HomeActivity, "Radio", Toast.LENGTH_SHORT).show()
                }
                R.id.action_search -> {
                    selectedFragment = SearchFragment()
                    tvFragment.text = "Tìm kiếm "
                    imgTopNav.setImageResource(R.drawable.ic_search)
                    Toast.makeText(this@HomeActivity, "Tìm kiếm", Toast.LENGTH_SHORT).show()
                }
            }
            supportFragmentManager.beginTransaction()
                                .replace(R.id.fragmentContainer, selectedFragment)
                                .commit()
            true
        }
    }

    //Ẩn navigation + status bar + fullscreen
    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, layoutHomeActivity).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior = WindowInsetsControllerCompat
                                            .BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun showInfoSong(){
        if (mSong==null) return

        Picasso.get().load(mSong.image).into(imgPlayNav)
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
//            val bundle = Bundle()
//            bundle.putSerializable("item_songs", mSong)
//            bundle.putBoolean("status_players", isPlaying)
//            intent.putExtras(bundle)

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

        sendActionToPlayMusicActivity(MyService.ACTION_START)
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
    private fun sendActionToPlayMusicActivity(action: Int){
        val intent = Intent("send_action_ata")
        val bundle = Bundle()

        // gửi dữ liệu đến activity
        bundle.putSerializable("item_songs", mSong)
        bundle.putBoolean("status_players", isPlaying)
        bundle.putInt("action_musics", action)

        intent.putExtras(bundle)

        sendBroadcast(intent)
        MyLib.showLog("HomeActivity: Send success!")
    }
}