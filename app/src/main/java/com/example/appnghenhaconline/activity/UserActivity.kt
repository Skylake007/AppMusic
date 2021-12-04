package com.example.appnghenhaconline.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.dataLocalManager.SharedPreferences.SessionUser
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {
    lateinit var btnBack: ImageView
    lateinit var viewInfo: LinearLayout
    lateinit var viewChangePass: LinearLayout
    lateinit var signOut : LinearLayout
    lateinit var tvName : TextView
    lateinit var image : CircleImageView
    private lateinit var session : SessionUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        MyLib.hideSystemUI(window, layoutUserActivity)
        session = SessionUser(applicationContext)
    }

    override fun onStart() {
        super.onStart()
        init(session)
        event(session)
    }

    override fun onBackPressed() {
        val a = Intent(this, HomeActivity::class.java)
        startActivity(a)

    }

    private fun init(session : SessionUser){
        val user = session.getUserDetails()
        viewInfo = findViewById(R.id.layout_info)
        viewChangePass = findViewById(R.id.layout_password)
        tvName = findViewById(R.id.tvName)
        image = findViewById(R.id.imgAvatar)
        tvName.text = user[session.KEY_NAME]
        MyLib.showLog("UserAvatar :"+ user[session.KEY_AVATAR]!!)
        Picasso.get().load(user[session.KEY_AVATAR]).error(R.drawable.img_error).into(image)
    }
    private fun event(session: SessionUser){
        btnBack = findViewById(R.id.btnBack)
        signOut = findViewById(R.id.SignOut)
        btnBack.setOnClickListener {
            finish()
        }
        viewInfo.setOnClickListener {
            intent = Intent(this, UserInfoActivity::class.java)
            startActivity(intent)
        }
        //set sự kiện hiện dialog
        viewChangePass.setOnClickListener {
            intent = Intent(this, UserPasswordActivity::class.java)
            startActivity(intent)
        }

        signOut.setOnClickListener {
            session.logoutUser()
        }
    }
}