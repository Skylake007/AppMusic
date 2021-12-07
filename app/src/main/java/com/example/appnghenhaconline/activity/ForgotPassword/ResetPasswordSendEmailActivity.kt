package com.example.appnghenhaconline.activity.ForgotPassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.activity.ForgotPassword.ResetPasswordSendEmailActivity
import com.example.appnghenhaconline.activity.LoginActivity
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.models.user.UserForgotPassword
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_reset_password_receive_email.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPasswordSendEmailActivity : AppCompatActivity() {
    lateinit var edtEmailForgotPassword : TextInputEditText
    lateinit var btnEmailForgotPassword : Button
    lateinit var btnBack : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password_receive_email)
        MyLib.hideSystemUI(window, resetPasswordSendEmailActivity)
    }

    override fun onStart() {
        super.onStart()
        init()
        event()
    }

    fun init() {
        edtEmailForgotPassword = findViewById(R.id.edtEmailForgotPassword)
        btnEmailForgotPassword = findViewById(R.id.btnSendEmail)
        btnBack = findViewById(R.id.btnBack)
    }

    fun event() {
        confirmEmail()
        backIntent()
    }

    private fun confirmEmail() {
        btnEmailForgotPassword.setOnClickListener {
            if (edtEmailForgotPassword.text.toString().trim() != "") {
                callApiSendEmailForgotPassword(edtEmailForgotPassword.text.toString().trim())
            }
            else {
                MyLib.showToast(this,"Vui lòng nhập email")
            }
        }
    }

    private fun backIntent(){
        btnBack.setOnClickListener {
            finish()
        }
    }

    private fun callApiSendEmailForgotPassword(email : String) {
        ApiService.apiService.sendEmailForgotPassword(email).enqueue(object : Callback<UserForgotPassword> {
            override fun onResponse(call: Call<UserForgotPassword>, response: Response<UserForgotPassword>) {
                val dataUserForgotPassword = response.body()
                if(dataUserForgotPassword != null) {
                    if (!dataUserForgotPassword.error) {
                        MyLib.showToast(this@ResetPasswordSendEmailActivity,dataUserForgotPassword.message)
                        intent = Intent(this@ResetPasswordSendEmailActivity,ResetPasswordSendResetCodeActivity::class.java)
                        intent.putExtra("email",dataUserForgotPassword.email)
                        startActivity(intent)
                    }
                    else {
                        MyLib.showToast(this@ResetPasswordSendEmailActivity,dataUserForgotPassword.message)
                    }
                }
            }

            override fun onFailure(call: Call<UserForgotPassword>, t: Throwable) {
                MyLib.showToast(this@ResetPasswordSendEmailActivity,"Call Api Error")
            }

        })
    }
}