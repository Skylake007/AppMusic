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
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.fragment.*
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.dataLocalManager.MyDataLocalManager
import com.example.appnghenhaconline.dataLocalManager.SharedPreferences.SessionUser
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.models.song.Song
import com.example.appnghenhaconline.dataLocalManager.Service.MyService
import com.example.appnghenhaconline.models.playlist.Playlist
import com.example.appnghenhaconline.models.user.DataUser
import com.example.appnghenhaconline.models.user.User
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import kotlin.math.abs

class HomeActivity : AppCompatActivity(), GestureDetector.OnGestureListener {

    private lateinit var gestureDetector : GestureDetector
    private lateinit var btnPlayOrPause : ImageView
    private lateinit var tvPlayNav : TextView
    private lateinit var menuUser: ImageView
    lateinit var mList: ArrayList<Song>
    lateinit var imgPlayNav : ImageView
    lateinit var session : SessionUser
    lateinit var btnNext : ImageView
    lateinit var btnPrev : ImageView
    lateinit var playNav : RelativeLayout

    private var backPressedTime: Long = 0
    private lateinit var backToast: Toast

    var mPosition: Int = 0
    var isPlaying: Boolean = false

    companion object{
        const val MIN_DISTANCE = 100
        const val MIN_VELOCITY = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        MyLib.hideSystemUI(window, layoutHomeActivity)
        session = SessionUser(applicationContext)
        val user = session.getUserDetails()
        MyLib.showLog("Passowrd: " + user[session.KEY_PASSWORD])
        checkUser(user[session.KEY_EMAIL]!!, user[session.KEY_PASSWORD]!!,session)
        init()
        initMenu()
    }

    override fun onStart() {
        super.onStart()
        session.checkLogin()
        initSongInfo()
        LocalBroadcastManager.getInstance(this).registerReceiver(broadcastReceiver,
                                                IntentFilter("send_action_to_activity"))
    }

    override fun onBackPressed() {
        if (backPressedTime + 2000 > System.currentTimeMillis()){
            backToast.cancel()

            val a = Intent(Intent.ACTION_MAIN)
            a.addCategory(Intent.CATEGORY_HOME)
            a.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(a)
        }else{
            backToast = Toast.makeText(this,"Chạm 2 lần để thoát ứng dụng !!!", Toast.LENGTH_SHORT)
            backToast.show()
        }
        backPressedTime = System.currentTimeMillis()
        
    }


    private fun init(){
        btnPlayOrPause = findViewById(R.id.btnPlayOrPause)
        imgPlayNav = findViewById(R.id.imgPlayNav)
        tvPlayNav = findViewById(R.id.tvPlayNav)
        menuUser = findViewById(R.id.setting)
        btnNext = findViewById(R.id.btnNext)
        btnPrev = findViewById(R.id.btnPre)
        playNav = findViewById(R.id.playNav)
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
        // chuyển sang userActivity
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
//        TODO("Not yet implemented")
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

//    private fun showInfoSong(){
//        Picasso.get().load(mList[mPosition].image).into(imgPlayNav)
//        tvPlayNav.text = mList[mPosition].title
//
//        btnPlayOrPause.setOnClickListener {
//            if (isPlaying){
//                sendActionToService(MyService.ACTION_PAUSE)
//            }else{
//                sendActionToService(MyService.ACTION_RESUME)
//            }
//        }
//        btnNext.setOnClickListener {
//            sendActionToService(MyService.ACTION_NEXT)
//        }
//        btnPrev.setOnClickListener {
//            sendActionToService(MyService.ACTION_PREVIOUS)
//        }
//    }

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

    private fun initSongInfo() {
        val songData: Song = MyDataLocalManager.getSong()
        val isPlayingData: Boolean = MyDataLocalManager.getIsPlaying()
        if (songData ==null && isPlayingData==null) {
            MyLib.showToast(this,"NULL")
            playNav.visibility = View.GONE
        }else{
            playNav.visibility = View.VISIBLE
            tvPlayNav.text = songData.title

            Picasso.get().load(songData.image)
//                  .resize(450,400)
                .into(imgPlayNav)

            if (isPlayingData){
                btnPlayOrPause.setImageResource(R.drawable.ic_baseline_pause_24)
            }else{
                btnPlayOrPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
            }

            btnPlayOrPause.setOnClickListener {
                if (isPlayingData){
                    sendActionToService(MyService.ACTION_PAUSE)
                }else{
                    sendActionToService(MyService.ACTION_RESUME)
                }
            }
            btnNext.setOnClickListener {
                sendActionToService(MyService.ACTION_NEXT)
            }
            btnPrev.setOnClickListener {
                sendActionToService(MyService.ACTION_PREVIOUS)
            }
        }

    }

    private var broadcastReceiver = object: BroadcastReceiver(){
        override fun onReceive(context: Context, intent: Intent) {
            val bundle: Bundle? = intent.extras
            //nhận dữ liệu action từ MyService
            mPosition = bundle?.get("position_song") as Int
            mList = bundle.get("list_song") as ArrayList<Song>
            isPlaying = bundle.getBoolean("status_player")
            val actionMusic: Int = bundle.getInt("action_music")

            handleLayoutMusic(actionMusic)
        }
    }

    private fun checkUser(username : String, password : String, sessionUser: SessionUser) { // call API LogIn check passowrd and load playlist
        ApiService.apiService.getLogIn(username, password).enqueue(object : Callback<DataUser?> {
            override fun onResponse(call: Call<DataUser?>, response: Response<DataUser?>) {
                val dataUser = response.body()
                if (dataUser != null) {
                    MyLib.showLog(dataUser.toString())

                    if (!dataUser.error) {
                        val user: User = dataUser.user
                        session.createLoginSession(user.id,user.name,user.email,user.sex,user.password)
                        var gson = Gson()
                        var listPlaylist = gson.toJson(dataUser.user.followPlaylist)
                        sessionUser.editor.putString(sessionUser.KEY_PLAYLIST,listPlaylist)
                        sessionUser.editor.commit()
                    }
                    else {
                        sessionUser.logoutUser()
                    }
                }
            }

            override fun onFailure(call: Call<DataUser?>, t: Throwable) {
                MyLib.showToast(this@HomeActivity,"Call Api Error")
            }
        })
    }

}