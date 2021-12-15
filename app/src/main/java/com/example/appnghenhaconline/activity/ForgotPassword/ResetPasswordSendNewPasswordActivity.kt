package com.example.appnghenhaconline.activity.ForgotPassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.activity.LoginActivity
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.models.user.UserForgotPassword
import kotlinx.android.synthetic.main.activity_reset_password_receive_email.*
import kotlinx.android.synthetic.main.activity_reset_password_send_new_password.*
import kotlinx.android.synthetic.main.activity_reset_password_send_reset_code.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPasswordSendNewPasswordActivity : AppCompatActivity() {
    lateinit var edtNewPassword : EditText
    lateinit var edtConfirmNewPassword : EditText
    lateinit var btnRestorePassword : Button
    lateinit var email : String
    lateinit var resendCode : String
    lateinit var btnBack : ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password_send_new_password)
        MyLib.hideSystemUI(window, resetPasswordSendNewPasswordActivity)
    }

    override fun onStart() {
        super.onStart()
        email = intent.getStringExtra("email").toString()
        resendCode = intent.getStringExtra("resetCode").toString()
        init()
        event()
    }

    fun init() {
        edtNewPassword = findViewById(R.id.edtNewPassword)
        edtConfirmNewPassword = findViewById(R.id.edtConfirmNewPassword)
        btnRestorePassword = findViewById(R.id.btnRestorePassword)
        btnBack = findViewById(R.id.btnBack)
    }


    fun event() {
        sendNewPassword()
        backIntent()
    }

    private fun backIntent(){
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun sendNewPassword() {
        btnRestorePassword.setOnClickListener {
            val newPassword = edtNewPassword.text.toString()
            val confirmNewPassword = edtConfirmNewPassword.text.toString()

            if (newPassword.trim() == "" || confirmNewPassword.trim() == "") {
                MyLib.showToast(this,"Vui lòng nhập đầy đủ thông tin")
            }
            else {
                when {
                    edtNewPassword.text.length < 6 -> {
                        MyLib.showToast(this, "Mật khẩu phải từ 6 kí tự trở lên")
                    }
                    newPassword != confirmNewPassword -> {
                        MyLib.showToast(this, "Kiểm tra lại mật khẩu và Xác nhận mật khẩu")
                    }
                    else -> {
                        var password = MyLib.md5(newPassword)
                        callApiSendNewPassword(email, resendCode, password)
                    }
                }
            }
        }
    }

    private fun callApiSendNewPassword(email : String, resetCode : String, password : String) {
        ApiService.apiService.sendNewPasswordForgotPassword(email, resetCode, password).enqueue(object :
            Callback<UserForgotPassword> {
            override fun onResponse(call: Call<UserForgotPassword>, response: Response<UserForgotPassword>) {
                val dataUserForgotPassword = response.body()
                if(dataUserForgotPassword != null) {
                    if (!dataUserForgotPassword.error) {
                        MyLib.showToast(this@ResetPasswordSendNewPasswordActivity,dataUserForgotPassword.message)
                        intent = Intent(this@ResetPasswordSendNewPasswordActivity,LoginActivity::class.java)
                        startActivity(intent)
                    }
                    else {
                        MyLib.showToast(this@ResetPasswordSendNewPasswordActivity,dataUserForgotPassword.message)
                    }
                }
            }

            override fun onFailure(call: Call<UserForgotPassword>, t: Throwable) {
                MyLib.showToast(this@ResetPasswordSendNewPasswordActivity,"Call Api Error")
            }

        })
    }
}