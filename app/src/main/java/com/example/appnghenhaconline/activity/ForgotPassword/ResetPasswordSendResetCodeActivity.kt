package com.example.appnghenhaconline.activity.ForgotPassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.activity.LoginActivity
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.models.user.UserForgotPassword
import kotlinx.android.synthetic.main.activity_reset_password_send_reset_code.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPasswordSendResetCodeActivity : AppCompatActivity() {
    lateinit var edtResetCode : EditText
    lateinit var btnConfirmResetCode : Button
    lateinit var tvResendResetCode : TextView
    lateinit var email : String
    lateinit var btnBack : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password_send_reset_code)
        MyLib.hideSystemUI(window, resetPasswordSendResetCodeActivity)
    }

    override fun onStart() {
        super.onStart()
        email = intent.getStringExtra("email").toString()
        init()
        event()
    }

    fun init () {
        edtResetCode = findViewById(R.id.edtResetcode)
        btnConfirmResetCode = findViewById(R.id.btnSendResetCode)
        tvResendResetCode = findViewById(R.id.tvResendResetCode)
        btnBack = findViewById(R.id.btnBack)
    }

    fun event() {
        countDownTimer()
        sendResetCode()
        backIntent()
    }

    private fun backIntent(){
        btnBack.setOnClickListener {
            val  intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun sendResetCode() {
        btnConfirmResetCode.setOnClickListener {
            if (edtResetCode.text.toString().trim() != "") {
                callApiSendResetCode(email,edtResetCode.text.toString().trim())
            }
            else {
                MyLib.showToast(this,"Vui lòng nhập mã xác nhận")
            }
        }
    }

    private fun callApiSendResetCode(email : String, resetCode : String) {
        ApiService.apiService.sendResetCodeForgotPassword(email, resetCode).enqueue(object :
            Callback<UserForgotPassword> {
            override fun onResponse(call: Call<UserForgotPassword>, response: Response<UserForgotPassword>) {
                val dataUserForgotPassword = response.body()
                if(dataUserForgotPassword != null) {
                    if (!dataUserForgotPassword.error) {
                        MyLib.showToast(this@ResetPasswordSendResetCodeActivity,dataUserForgotPassword.message)
                        intent = Intent(this@ResetPasswordSendResetCodeActivity,ResetPasswordSendNewPasswordActivity::class.java)
                        intent.putExtra("email",email)
                        intent.putExtra("resetCode",resetCode)
                        startActivity(intent)
                    }
                    else {
                        MyLib.showToast(this@ResetPasswordSendResetCodeActivity,dataUserForgotPassword.message)
                    }
                }
            }

            override fun onFailure(call: Call<UserForgotPassword>, t: Throwable) {
                MyLib.showToast(this@ResetPasswordSendResetCodeActivity,"Call Api Error")
            }

        })
    }

    private fun callApiSendEmailForgotPassword(email : String) {
        ApiService.apiService.sendEmailForgotPassword(email).enqueue(object :
            Callback<UserForgotPassword> {
            override fun onResponse(call: Call<UserForgotPassword>, response: Response<UserForgotPassword>) {
                val dataUserForgotPassword = response.body()
                if(dataUserForgotPassword != null) {
                    if (!dataUserForgotPassword.error) {
                        MyLib.showToast(this@ResetPasswordSendResetCodeActivity,dataUserForgotPassword.message)
                    }
                    else {
                        MyLib.showToast(this@ResetPasswordSendResetCodeActivity,dataUserForgotPassword.message)
                    }
                }
            }

            override fun onFailure(call: Call<UserForgotPassword>, t: Throwable) {
                MyLib.showToast(this@ResetPasswordSendResetCodeActivity,"Call Api Error")
            }

        })
    }

    private fun countDownTimer() {
        var timer = object : CountDownTimer(60000,1000) {
            override fun onTick(millisUntilFinished: Long) {
                tvResendResetCode.text = "Thử lại sau " + millisUntilFinished / 1000
            }

            override fun onFinish() {
                tvResendResetCode.text = "Thử lại"
                tvResendResetCode.setOnClickListener {
                    callApiSendEmailForgotPassword(email)
                    countDownTimer()
                }
            }
        }
        timer.start()
    }

}