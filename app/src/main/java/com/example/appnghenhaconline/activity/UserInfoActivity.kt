package com.example.appnghenhaconline.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.models.user.DataUser
import com.example.appnghenhaconline.models.user.DataUserSignUp
import com.example.appnghenhaconline.models.user.UpdateUser
import com.example.appnghenhaconline.models.user.User
import com.google.android.material.textfield.TextInputEditText
import kotlinx.android.synthetic.main.activity_user_info.*
import kotlinx.android.synthetic.main.activity_user_info.etSex
import kotlinx.android.synthetic.main.fm_signup_tab_fragment.*
import kotlinx.android.synthetic.main.fm_signup_tab_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UserInfoActivity : AppCompatActivity() {
    lateinit var btnBack: ImageView
    lateinit var edtSex: AutoCompleteTextView
    lateinit var edtName : TextInputEditText
    lateinit var edtEmail : TextInputEditText
    lateinit var btnSaveInfo : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        MyLib.hideSystemUI(window, layoutUserInfoActivity)
        val user = intent.getSerializableExtra("User") as? User
        init(user!!)
        event(user!!)
    }


    private fun init(user : User){
        btnBack = findViewById(R.id.btnBackUserInfo)
        edtSex = findViewById(R.id.etSex)
        edtName = findViewById(R.id.edtName)
        edtEmail = findViewById(R.id.edtEmail)
        btnSaveInfo = findViewById(R.id.btnSaveInfo)
        edtName.setText(user.name)
        edtEmail.setText(user.email)

        if (user.sex) {
            edtSex.setText("Nam")
        }
        else {
            edtSex.setText("Nữ")
        }

        val sex = resources.getStringArray(R.array.sex)
        val arrAdapter = ArrayAdapter(this, R.layout.i_dropdown_sex_item, sex)
        edtSex.setAdapter(arrAdapter)

        btnSaveInfo.setOnClickListener {

            val name = edtName.text.toString()
            if (name.trim() == "") {
                MyLib.showToast(this,"Vui lòng nhập tên người dùng")
            }
            else {
                val sex: Boolean = edtSex.text.toString() == "Nam"
                callApiUpdateUser(user.email,name,sex)
            }
        }
    }

    private fun callApiUpdateUser( email : String, name : String, sex : Boolean) { // call API UpdateUser
        ApiService.apiService.putUdateUser(email,name,sex).enqueue(object :
            Callback<UpdateUser> {
            override fun onResponse(call: Call<UpdateUser>, response: Response<UpdateUser>) {
                val dataUser  = response.body()
                if(dataUser != null) {
                    if (!dataUser.error){
                        val user : User = dataUser.user
                        MyLib.showLog(dataUser.toString())
                        MyLib.showToast(this@UserInfoActivity,dataUser.message)
                        intent = Intent(this@UserInfoActivity, UserActivity::class.java)
                        intent.putExtra("User",user)
                        startActivity(intent)
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

    private fun event(user : User){
        btnBack.setOnClickListener {
            intent = Intent(this@UserInfoActivity, UserActivity::class.java)
            intent.putExtra("User",user)
            startActivity(intent)
        }
    }
}