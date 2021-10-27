package com.example.appnghenhaconline.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.appnghenhaconline.activity.HomeActivity
import com.example.appnghenhaconline.R

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
        btnLogin.setOnClickListener {
            val intent = Intent(view.context, HomeActivity::class.java)
            startActivity(intent)
        }
    }
}