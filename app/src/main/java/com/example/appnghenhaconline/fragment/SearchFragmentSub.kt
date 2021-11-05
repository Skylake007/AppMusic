package com.example.appnghenhaconline.fragment

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.appnghenhaconline.R
import androidx.core.content.ContextCompat.getSystemService




class SearchFragmentSub: Fragment() {

    internal lateinit var view: View
    lateinit var edtSearch: EditText
    lateinit var btnBack: ImageView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fm_search_fragment_sub, container, false)
        init()
        event()
        return view
    }

    private fun init(){
        edtSearch = view.findViewById(R.id.edtSearch)
        btnBack = view.findViewById(R.id.btnBack)
    }

    private fun event(){
        //auto focus vào edittext
        edtSearch.requestFocus()
        edtSearch.showSoftKeyboard()
        //set back cho button
        btnBack.setOnClickListener {
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainer, SearchFragment())
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }
    }
    private fun EditText.showSoftKeyboard(){
        (this.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
            .toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
    }
}