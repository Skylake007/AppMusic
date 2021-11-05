package com.example.appnghenhaconline.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.models.user.DataUserSignUp
import kotlinx.android.synthetic.main.fm_signup_tab_fragment.*
import kotlinx.android.synthetic.main.fm_signup_tab_fragment.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SignupTabFragment : Fragment() {

    internal lateinit var view : View

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fm_signup_tab_fragment, container, false)

        val sex = resources.getStringArray(R.array.sex)
        val arrAdapter = ArrayAdapter(requireContext(), R.layout.i_dropdown_sex_item, sex)
        view.etSex.setAdapter(arrAdapter)
        event()
        return view
    }

    private fun event() {
        val btnSignup : Button = view.findViewById(R.id.btnSignUp)
        val firstName : EditText = view.findViewById(R.id.first_name)
        val lastName : EditText = view.findViewById(R.id.last_name)
        val email : EditText = view.findViewById(R.id.email)
        val password : EditText = view.findViewById(R.id.password)
        val confirmPassword : EditText = view.findViewById(R.id.confirm_password)

        btnSignup.setOnClickListener {
            if(first_name.text.toString() == ""
                || last_name.text.toString() == ""
                || email.text.toString() == ""
                || password.text.toString() == ""
                || confirmPassword.text.toString() == ""
                || etSex.text.toString() == "") {
                MyLib.showToast(requireContext(),"Vui lòng nhập đầy đủ thông tin")
            }
            else {
                if(password.text.toString() != confirmPassword.text.toString()) {
                    MyLib.showToast(requireContext(),"Kiểm tra lại mật khẩu và Xác nhận mật khẩu")
                }
                else {
                    val sex: Boolean = etSex.text.toString() == "Nam"
                    val name = firstName.text.toString() + " " + lastName.text.toString()
                    val encryptPassword = MyLib.md5(password.text.toString())

                    callApiSignIn(name,encryptPassword,sex,email.text.toString())
                }
            }

        }
    }

    private fun callApiSignIn(name : String, password : String, sex : Boolean, email : String) { // call API SignIn
        ApiService.apiService.postSignUp(name,password,sex,email).enqueue(object : Callback<DataUserSignUp> {
            override fun onResponse(call: Call<DataUserSignUp>, response: Response<DataUserSignUp>) {
                val dataUserSignUp : DataUserSignUp? = response.body()
                if(dataUserSignUp != null) {
                    if (!dataUserSignUp.error){
                        MyLib.showToast(requireContext(),dataUserSignUp.message)
                    }
                    else {
                        MyLib.showToast(requireContext(),dataUserSignUp.message)
                    }
                }
            }

            override fun onFailure(call: Call<DataUserSignUp>, t: Throwable) {
                MyLib.showToast(requireContext(),"Call Api Error")
            }
        })
    }
}