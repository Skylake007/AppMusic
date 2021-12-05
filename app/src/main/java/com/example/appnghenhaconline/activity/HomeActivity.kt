package com.example.appnghenhaconline.activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
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
import com.example.appnghenhaconline.fragment.library.LibraryFragment
import com.example.appnghenhaconline.fragment.playNow.PlayNowFragment
import com.example.appnghenhaconline.fragment.search.SearchFragment
import com.example.appnghenhaconline.models.user.DataUser
import com.example.appnghenhaconline.models.user.User
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fm_play_now_fragment.*
import kotlinx.android.synthetic.main.tab_album_of_singer.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
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
    //===============================================
    //region broadcastReceiver
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
    //endregion
    //===============================================
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        session = SessionUser(applicationContext)
        val user = session.getUserDetails()
        checkUser(user[session.KEY_EMAIL]!!, user[session.KEY_PASSWORD]!!,session)

        init()
        playNav.visibility = View.GONE
    }

    override fun onStart() {
        super.onStart()
        session.checkLogin()
        //initSongInfo()
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

    override fun onDestroy() {
        sendActionToService(MyService.ACTION_CLEAR)
        LocalBroadcastManager.getInstance(this).unregisterReceiver(broadcastReceiver)
        super.onDestroy()
    }

    //===============================================
    //region INIT
    private fun init(){
        btnPlayOrPause = findViewById(R.id.btnPlayOrPause)
        imgPlayNav = findViewById(R.id.imgPlayNav)
        tvPlayNav = findViewById(R.id.tvPlayNav)
        menuUser = findViewById(R.id.setting)
        btnNext = findViewById(R.id.btnNext)
        btnPrev = findViewById(R.id.btnPre)
        playNav = findViewById(R.id.playNav)
        initMenu()
        MyLib.hideSystemUI(window, layoutHomeActivity)
    }

    private fun initMenu(){
        //mặc định load trang là PlayNowFragment
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, PlayNowFragment()).apply {
                tvFragment.setText(R.string.action_play_now)
                imgTopNav.setImageResource(R.drawable.ic_play_circle_white)
            }.commit()
        //set sự kiện chuyển fragment
        bottomNav.setOnNavigationItemSelectedListener { item ->
            var selectedFragment: Fragment = PlayNowFragment()
            when (item.itemId) {
                R.id.action_play_now -> {
                    selectedFragment = PlayNowFragment()
                    tvFragment.setText(R.string.action_play_now)
                    imgTopNav.setImageResource(R.drawable.ic_play_circle_white)

                    this.currentFocus?.let { view ->
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                        imm?.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                }
                R.id.action_radio -> {
                    selectedFragment = LibraryFragment()
                    tvFragment.setText(R.string.action_library)
                    imgTopNav.setImageResource(R.drawable.ic_library)

                    this.currentFocus?.let { view ->
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                        imm?.hideSoftInputFromWindow(view.windowToken, 0)
                    }
                }
                R.id.action_search -> {
                    selectedFragment = SearchFragment()
                    tvFragment.setText(R.string.action_search)
                    imgTopNav.setImageResource(R.drawable.ic_search)

                    this.currentFocus?.let { view ->
                        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                        imm?.hideSoftInputFromWindow(view.windowToken, 0)
                    }
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
    //endregion
    //===============================================
    //region SỰ KIỆN VUỐT MÀN HÌNH
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
    //endregion
    //===============================================
    //region SERVICE
    //set hành động sau khi nhận từ service
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

    //set trạng thái button play
    private fun setStatusButtonPlayOrPause(){
        if (isPlaying){
            btnPlayOrPause.setImageResource(R.drawable.ic_baseline_pause_24)
        }else{
            btnPlayOrPause.setImageResource(R.drawable.ic_baseline_play_arrow_24)
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
    //region ANOTHER FUNC
    //set thông tin bài hát
    private fun initSongInfo() {
        val songData: Song = MyDataLocalManager.getSong()
        val isPlayingData: Boolean = MyDataLocalManager.getIsPlaying()
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
    //endregion
    //===============================================
    //region CALL API
    // call API LogIn check passowrd and load playlist
    private fun checkUser(username : String, password : String, sessionUser: SessionUser) {
        ApiService.apiService.getLogIn(username, password).enqueue(object : Callback<DataUser?> {
            override fun onResponse(call: Call<DataUser?>, response: Response<DataUser?>) {
                val dataUser = response.body()
                if (dataUser != null) {
                    MyLib.showLog(dataUser.toString())

                    if (!dataUser.error) {
                        val user: User = dataUser.user
                        session.createLoginSession(user.id,user.name,user.email,user.sex,user.password)
                        val gson = Gson()
                        val listPlaylist = gson.toJson(dataUser.user.followPlaylist)
                        val listAlbum = gson.toJson(dataUser.user.followAlbum)
                        val listSinger = gson.toJson(dataUser.user.favoriteSinger)
                        sessionUser.editor.putString(sessionUser.KEY_PLAYLIST,listPlaylist)
                        sessionUser.editor.putString(sessionUser.KEY_ALBUM,listAlbum)
                        sessionUser.editor.putString(sessionUser.KEY_SINGER,listSinger)
                        sessionUser.editor.putString(sessionUser.KEY_AVATAR,dataUser.user.avatar)
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
    //endregion
    //===============================================
}