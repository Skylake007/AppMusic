package com.example.appnghenhaconline.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import com.example.appnghenhaconline.R
import kotlinx.android.synthetic.main.signup_tab_fragment.view.*

class SignupTabFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.signup_tab_fragment, container, false)

        val sex = resources.getStringArray(R.array.sex)
        val arrAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_sex_item, sex)
        view.auto_complete_textview.setAdapter(arrAdapter)

        return view
    }
}