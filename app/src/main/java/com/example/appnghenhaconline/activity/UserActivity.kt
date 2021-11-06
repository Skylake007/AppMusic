package com.example.appnghenhaconline.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.Window
import android.view.WindowManager
import android.view.WindowManager.LayoutParams.MATCH_PARENT
import android.view.WindowManager.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_user.*

class UserActivity : AppCompatActivity() {
    lateinit var btnBack: ImageView
    lateinit var viewInfo: LinearLayout
    lateinit var viewChangePass: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)
        MyLib.hideSystemUI(window, layoutUserActivity)
        init()
        event()
    }

    private fun init(){
        viewInfo = findViewById(R.id.layout_info)
        viewChangePass = findViewById(R.id.layout_password)
    }
    private fun event(){
        btnBack = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            onBackPressed()
        }
        viewInfo.setOnClickListener {
            intent = Intent(this, UserInfoActivity::class.java)
            startActivity(intent)
        }
        //set sự kiện hiện dialog
        viewChangePass.setOnClickListener {
            openDialogConfirmPassword(Gravity.CENTER)
        }
    }

    //hàm hiện dialog
    private fun openDialogConfirmPassword(gravity: Int){
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dlg_confirm_password_dialog)
        dialog.setCancelable(true)

        val window = dialog.window
        window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val windowAttributes: WindowManager.LayoutParams = window!!.attributes
        windowAttributes.gravity = gravity
        window.attributes = windowAttributes

        val btnExit: Button = dialog.findViewById(R.id.btnExit)
        val btnAccept: Button = dialog.findViewById(R.id.btnAccept)
//        var edtPassword: TextInputEditText = dialog.findViewById(R.id.edtPassword)

        btnExit.setOnClickListener {
            dialog.dismiss()
        }
        btnAccept.setOnClickListener {
            intent = Intent(this, UserPasswordActivity::class.java)
            startActivity(intent)
        }
        dialog.show()
    }
}