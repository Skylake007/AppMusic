package com.example.appnghenhaconline.activity.ForgotPassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.activity.LoginActivity
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.models.user.UserForgotPassword
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPasswordSendNewPasswordActivity : AppCompatActivity() {
    lateinit var edtNewPassword : EditText
    lateinit var edtConfirmNewPassword : EditText
    lateinit var btnRestorePassword : Button
    lateinit var email : String
    lateinit var resendCode : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password_send_new_password)
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
    }

    fun event() {
        sendNewPassword()
    }

    private fun sendNewPassword() {
        btnRestorePassword.setOnClickListener {
            if (edtNewPassword.text.toString().trim() == "" || edtConfirmNewPassword.text.toString().trim() == "") {
                MyLib.showToast(this,"Vui lòng nhập đầy đủ thông tin")
            }
            else {
                if (edtNewPassword.text.toString().trim() == edtConfirmNewPassword.text.toString().trim()) {
                    var password = MyLib.md5(edtNewPassword.text.toString().trim())
                    callApiSendNewPassword(email,resendCode,password)
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