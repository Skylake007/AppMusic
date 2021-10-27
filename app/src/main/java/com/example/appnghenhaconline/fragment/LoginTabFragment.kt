package com.example.appnghenhaconline.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.models.user.DataUser
import com.example.appnghenhaconline.models.user.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginTabFragment: Fragment() {

    internal lateinit var view: View


    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.login_tab_fragment, container, false)

        event()
        return view
    }

    private fun event(){
        val btnLogin: Button = view.findViewById(R.id.btnLogin)
        var username : EditText = view.findViewById(R.id.etEmail)
        var password : EditText = view.findViewById(R.id.etPassword)
        btnLogin.setOnClickListener {

            callApiSignIn(username.text.toString(),password.text.toString())
        }
    }

    private fun callApiSignIn(username : String, password : String) { // call API SignIn
        Log.e(null,username.toString() +"\n" + password.toString())
        ApiService.apiService.getSignIn(username, password).enqueue(object : Callback<DataUser?> {
            override fun onResponse(call: Call<DataUser?>, response: Response<DataUser?>) {
                val dataUser = response.body()
                Log.e(null, dataUser.toString())
                if (dataUser != null) {
                    if (!dataUser!!.error) {
                        val listUser: ArrayList<User> = dataUser.listUser


                        Toast.makeText(context,dataUser.message,Toast.LENGTH_SHORT).show()
                    }
                    else {
                        Toast.makeText(context,dataUser.message,Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<DataUser?>, t: Throwable) {
                Toast.makeText(context, "Call Api Error",Toast.LENGTH_SHORT).show()
            }

        })
    }
}