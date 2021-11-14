package com.example.appnghenhaconline.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.dataLocalManager.SharedPreferences.SessionUser
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.models.user.UpdateUser
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_user_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserPasswordActivity : AppCompatActivity() {
    lateinit var btnBack: ImageView
    lateinit var tpOldPassword : TextInputEditText
    lateinit var tpNewPassword : TextInputEditText
    lateinit var tpConfirmNewPassword : TextInputEditText
    lateinit var btnSaveUserPassowrd : ImageView
    private lateinit var session : SessionUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_password)
        MyLib.hideSystemUI(window, layoutUserPasswordActivity)
        session = SessionUser(applicationContext)
        init(session)
        event()
    }

    private fun init(session : SessionUser) {
        val user = session.getUserDetails()
        tpOldPassword = findViewById(R.id.oldPassword)
        tpNewPassword = findViewById(R.id.newPassword)
        tpConfirmNewPassword = findViewById(R.id.confirmNewPassowrd)
        btnSaveUserPassowrd = findViewById(R.id.btnSavePassword)
        btnBack = findViewById(R.id.btnBackUserPassword)

        btnSaveUserPassowrd.setOnClickListener {
            val oldPassword = tpOldPassword.text.toString()
            val newPassowrd = tpNewPassword.text.toString()
            val confirmPassword = tpConfirmNewPassword.text.toString()
            if (oldPassword == "" || newPassowrd == "" || confirmPassword == "") {
                MyLib.showToast(this,"Vui lòng điền đầy đủ mật khẩu")
            }
            else {
                if (newPassowrd != confirmPassword) {
                    MyLib.showToast(this,"Mật khẩu và xác nhận mật khẩu không khớp")
                }
                else {
                    val encryptOldPassword = MyLib.md5(oldPassword)
                    val encryptNewPassword = MyLib.md5(newPassowrd)
                    MyLib.showLog(encryptOldPassword + "\n" + encryptNewPassword)
                    callApiUpdateUserPassword(user[session.KEY_EMAIL]!!,encryptOldPassword,encryptNewPassword,session)
                }
            }
        }

    }

    private fun callApiUpdateUserPassword( email : String, oldPassword : String, newPassowrd : String, session: SessionUser) { // call API UpdateUser
        ApiService.apiService.putUpdateUserPassword(email,oldPassword,newPassowrd).enqueue(object : Callback<UpdateUser> {
            override fun onResponse(call: Call<UpdateUser>, response: Response<UpdateUser>) {
                val dataUser  = response.body()
                if(dataUser != null) {
                    if (!dataUser.error){
                        val user = dataUser.user
                        session.editor.putString(session.KEY_PASSWORD,user.password)
                        session.editor.commit()
                        MyLib.showToast(this@UserPasswordActivity,dataUser.message)
                        intent = Intent(this@UserPasswordActivity, UserActivity::class.java)
                        startActivity(intent)
                    }
                    else {
                        MyLib.showToast(this@UserPasswordActivity,dataUser.message)
                    }
                }
            }

            override fun onFailure(call: Call<UpdateUser>, t: Throwable) {
                MyLib.showToast(this@UserPasswordActivity,"Call Api Error")
            }
        })
    }

    private fun event() {
        btnBack.setOnClickListener {
            intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
        }
    }
}