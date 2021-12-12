package com.example.appnghenhaconline.activity

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import androidx.core.graphics.rotationMatrix
import com.bumptech.glide.Glide
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.dataLocalManager.RealPathUtil
import com.example.appnghenhaconline.dataLocalManager.SharedPreferences.SessionUser
import com.example.appnghenhaconline.models.user.DataUser
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_user.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException

class UserActivity : AppCompatActivity() {
    lateinit var btnBack: ImageView
    lateinit var viewInfo: LinearLayout
    lateinit var viewChangePass: LinearLayout
    lateinit var signOut : LinearLayout
    lateinit var tvName : TextView
    lateinit var image : CircleImageView
    private val IMAGE_PICK_CODE = 100
    private val PERMISSION_CODE = 1001
    private lateinit var mUri : Uri
    private lateinit var progressDialog: ProgressDialog
    private lateinit var session : SessionUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        MyLib.hideSystemUI(window, layoutUserActivity)
        session = SessionUser(applicationContext)
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Vui lòng đợi .....")
    }

    override fun onStart() {
        super.onStart()
        init(session)
        event(session)
    }


    override fun onBackPressed() {
        finish()
    }

    private fun init(session : SessionUser){
        val user = session.getUserDetails()
        mUri = Uri.EMPTY
        viewInfo = findViewById(R.id.layout_info)
        viewChangePass = findViewById(R.id.layout_password)
        tvName = findViewById(R.id.tvName)
        image = findViewById(R.id.imgAvatar)
        tvName.text = user[session.KEY_NAME]
//        Picasso.get().load(user[session.KEY_AVATAR]).error(R.drawable.img_error).into(image)
        Glide.with(this@UserActivity)
            .load(user[session.KEY_AVATAR])
            .error(R.drawable.img_avatar_4)
            .into(image)
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
        clickPicture()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openGallery()
                }
                else {
                    MyLib.showToast(this,"Permission denied")
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            val uri = data?.data
            mUri = uri!!

            try{
                callApiUpdateAvatarUser()
                init(session)
            }
            catch(e : IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    private fun clickPicture() {
        image.setOnClickListener {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED )  {
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                    requestPermissions(permissions, PERMISSION_CODE);
                }
                else {
                    openGallery()
                }
            }
            else {
                openGallery()
            }
        }
    }

    private fun callApiUpdateAvatarUser() {
        progressDialog.show()
        val user = session.getUserDetails()
        val userId = user[session.KEY_ID]

        val requestBodyUserId = RequestBody.create(MediaType.parse("multipart/form-data"),userId)

        val strRealPath = RealPathUtil.getRealPath(this, mUri);
        MyLib.showLog("Xem thử đường link thế nào: $strRealPath")
        val file = File(strRealPath)
        val requestBodyAvatar = RequestBody.create(MediaType.parse("multipart/form-data"),file)
        val multipartBodyAvatar = MultipartBody.Part.createFormData("image",file.name, requestBodyAvatar)

        ApiService.apiService.updateAvatarUser(multipartBodyAvatar,requestBodyUserId)
                                                    .enqueue( object : Callback<DataUser> {
            override fun onResponse(call: Call<DataUser>, response: Response<DataUser>) {
                progressDialog.dismiss()
                val dataUser = response.body()
                if (dataUser != null) {
                    if (!dataUser.error) {
                        MyLib.showToast(this@UserActivity,dataUser.message)
                        session.editor.putString(session.KEY_AVATAR,dataUser.user.avatar)
                        session.editor.commit()

//                        Picasso.get().load(dataUser.user.avatar).fit().centerInside().rotate(90f).error(R.drawable.img_error).into(image)
                        Glide.with(this@UserActivity)
                            .load(dataUser.user.avatar)
                            .error(R.drawable.img_error)
                            .into(image)

                    }
                    else {
                        MyLib.showToast(this@UserActivity,dataUser.message)
                    }
                }
            }

            override fun onFailure(call: Call<DataUser>, t: Throwable) {
                progressDialog.dismiss()
                MyLib.showToast(this@UserActivity,"Call Api Error")
            }
        })
    }
}