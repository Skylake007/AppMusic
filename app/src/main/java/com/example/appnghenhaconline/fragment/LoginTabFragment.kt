package com.example.appnghenhaconline.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.dataLocalManager.SharedPreferences.SessionUser
import com.example.appnghenhaconline.activity.HomeActivity
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.models.user.DataUser
import com.example.appnghenhaconline.models.user.User
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginTabFragment: Fragment() {
    internal lateinit var view: View
    lateinit var session : SessionUser

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        view = inflater.inflate(R.layout.fm_login_tab_fragment, container, false)
        session = SessionUser(this.requireContext())
        event(session)
        return view
    }

    private fun event(session : SessionUser){
        val btnLogin: Button = view.findViewById(R.id.btnLogin)
        val username : TextInputEditText = view.findViewById(R.id.edtEmail)
        val password : TextInputEditText = view.findViewById(R.id.edtPassword)
        btnLogin.setOnClickListener {
            if (username.text.toString().trim() == "" || password.text.toString().trim() == "") {
                MyLib.showToast(requireContext(),"Vui lòng nhập đầy đủ thông tin")
            }
            else {
                val encryptPassword = MyLib.md5(password.text.toString())
                callApiSignIn(username.text.toString(), encryptPassword, session)
            }
        }
    }

    private fun callApiSignIn(username : String, password : String, session: SessionUser) { // call API LogIn
        Log.e(null,username.toString() +"\n" + password.toString())
        ApiService.apiService.getLogIn(username, password).enqueue(object : Callback<DataUser?> {
            override fun onResponse(call: Call<DataUser?>, response: Response<DataUser?>) {
                val dataUser = response.body()
                Log.e(null, dataUser.toString())
                if (dataUser != null) {
                    if (!dataUser.error) {
                        val user: User = dataUser.user
                        MyLib.showToast(requireContext(),dataUser.message)
                        var intent = Intent(requireContext(),HomeActivity::class.java)
                        session.createLoginSession(user.name,user.email,user.sex)
                        startActivity(intent)
                    }
                    else {
                        MyLib.showToast(requireContext(),dataUser.message)
                    }
                }
            }

            override fun onFailure(call: Call<DataUser?>, t: Throwable) {
                MyLib.showToast(requireContext(),"Call Api Error")
            }
        })
    }
}