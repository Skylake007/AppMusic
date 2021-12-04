package com.example.appnghenhaconline.activity

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.*
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.dataLocalManager.SharedPreferences.SessionUser
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.dataLocalManager.RealPathUtil
import com.example.appnghenhaconline.models.user.DataUser
import com.example.appnghenhaconline.models.user.UpdateUser
import com.example.appnghenhaconline.models.user.User
import com.google.android.material.textfield.TextInputEditText
import com.squareup.picasso.Picasso
import com.squareup.picasso.Request
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.android.synthetic.main.fm_signup_tab_fragment.*
import kotlinx.android.synthetic.main.fm_signup_tab_fragment.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException

class UserInfoActivity : AppCompatActivity() {
    lateinit var btnBack: ImageView
    lateinit var edtSex: AutoCompleteTextView
    lateinit var edtName : TextInputEditText
    lateinit var edtEmail : TextInputEditText
    lateinit var btnSaveInfo : ImageView
    lateinit var image : CircleImageView
    private val IMAGE_PICK_CODE = 100
    private val PERMISSION_CODE = 1001
    private lateinit var mUri : Uri
    private lateinit var progressDialog: ProgressDialog
    private lateinit var session : SessionUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        MyLib.hideSystemUI(window, layoutUserInfoActivity)
        session = SessionUser(applicationContext)
        init(session)
        progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Vui lòng đợi .....")
    }

    override fun onStart() {
        super.onStart()
        event()
    }

    private fun init(session : SessionUser){
        val user = session.getUserDetails()
        mUri = Uri.EMPTY
        btnBack = findViewById(R.id.btnBackUserInfo)
        edtSex = findViewById(R.id.etSex)
        edtName = findViewById(R.id.edtName)
        edtEmail = findViewById(R.id.edtEmail)
        btnSaveInfo = findViewById(R.id.btnSaveInfo)
        image = findViewById(R.id.imgAvatar)
        edtName.setText(user[session.KEY_NAME])
        edtEmail.setText(user[session.KEY_EMAIL])

        Picasso.get().load(user[session.KEY_AVATAR]).error(R.drawable.img_error).into(image)

        if (user[session.KEY_SEX].toBoolean()) {
            edtSex.setText("Nam")
        }
        else {
            edtSex.setText("Nữ")
        }

        val sex = resources.getStringArray(R.array.sex)
        val arrAdapter = ArrayAdapter(this, R.layout.i_dropdown_sex_item, sex)
        edtSex.setAdapter(arrAdapter)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
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
            var uri = data?.data
            mUri = uri!!
            try{
                var bitmap = MediaStore.Images.Media.getBitmap(contentResolver,uri)
                image.setImageBitmap(bitmap)
            }
            catch(e : IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun event(){
        btnBack.setOnClickListener {
            finish()
        }
        clickPicture()
        clickSave()
    }

    private fun clickPicture() {
        image.setOnClickListener {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED )  {
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

    private fun clickSave() {
        btnSaveInfo.setOnClickListener {
            val user = session.getUserDetails()
            val name = edtName.text.toString()
            val sex: Boolean = edtSex.text.toString() == "Nam"
            if (name.trim() == "") {
                MyLib.showToast(this,"Vui lòng nhập tên người dùng")
            }
            else {
                if (mUri != Uri.EMPTY) {
                    callApiUpdateUser(user[session.KEY_EMAIL]!!,name,sex, session)
                    callApiUpdateAvatarUser()

                }
                else {
                    callApiUpdateUser(user[session.KEY_EMAIL]!!,name,sex, session)
                    finish()
                }
            }
        }
    }

    private fun callApiUpdateAvatarUser() {
        progressDialog.show()

        val user = session.getUserDetails()
        var userId = user[session.KEY_ID]

        var requestBodyUserId = RequestBody.create(MediaType.parse("multipart/form-data"),userId)

        var strRealPath = RealPathUtil.getRealPath(this, mUri);
        MyLib.showLog("Xem thử đường link thế nào: $strRealPath")
        var file = File(strRealPath)
        var requestBodyAvata = RequestBody.create(MediaType.parse("multipart/form-data"),file)
        var multipartBodyAvatar = MultipartBody.Part.createFormData("image",file.name, requestBodyAvata)

        ApiService.apiService.updateAvatarUser(multipartBodyAvatar,requestBodyUserId).enqueue( object : Callback<DataUser> {
            override fun onResponse(call: Call<DataUser>, response: Response<DataUser>) {
                progressDialog.dismiss()
                val dataUser = response.body()
                if (dataUser != null) {
                    if (!dataUser.error) {
                        MyLib.showToast(this@UserInfoActivity,dataUser.message)
                        session.editor.putString(session.KEY_AVATAR,dataUser.user.avatar)
                        session.editor.commit()
                        finish()
                    }
                    else {
                        MyLib.showToast(this@UserInfoActivity,dataUser.message)
                    }
                }
            }

            override fun onFailure(call: Call<DataUser>, t: Throwable) {
                progressDialog.dismiss()
                MyLib.showToast(this@UserInfoActivity,"Call Api Error")
            }
        })
    }

    private fun callApiUpdateUser( email : String, name : String, sex : Boolean, session: SessionUser)  { // call API UpdateUser
        ApiService.apiService.putUpdateUser(email,name,sex).enqueue(object : Callback<UpdateUser> {
            override fun onResponse(call: Call<UpdateUser>, response: Response<UpdateUser>) {
                val dataUser  = response.body()
                if(dataUser != null) {
                    if (!dataUser.error){
                        val user : User = dataUser.user
                        MyLib.showToast(this@UserInfoActivity,dataUser.message)
                        session.editor.putString(session.KEY_NAME,user.name)
                        session.editor.putBoolean(session.KEY_SEX,user.sex)
                        session.editor.commit()
                    }
                    else {
                        MyLib.showToast(this@UserInfoActivity,dataUser.message)
                    }
                }
            }

            override fun onFailure(call: Call<UpdateUser>, t: Throwable) {
                MyLib.showToast(this@UserInfoActivity,"Call Api Error")
            }
        })
    }


}