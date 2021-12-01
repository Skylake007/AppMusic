package com.example.appnghenhaconline.fragment.SingerInfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.adapter.TabOfSingerAdapter
import com.example.appnghenhaconline.fragment.SearchFragment
import com.example.appnghenhaconline.models.singer.Singer
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso

class SingerInfoFragment: Fragment() {
    internal lateinit var view: View
    private lateinit var tvSingerName: TextView
    private lateinit var imgSinger: ImageView
    private lateinit var idSinger: String
    private lateinit var btnBack: ImageView
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                savedInstanceState: Bundle?): View? {

        view = inflater.inflate(R.layout.fm_singer_info_fragment, container, false)
        init()
        return view
    }

    private fun init() {
        tvSingerName = view.findViewById(R.id.tvSingerName)
        imgSinger = view.findViewById(R.id.imgSinger)
        btnBack = view.findViewById(R.id.btnBack)
        viewPager = view.findViewById(R.id.view_pager)
        tabLayout = view.findViewById(R.id.tab_layout)
        initInfoSinger()
        initTabFragment()
        event()
    }

    private fun event() {
        btnBack.setOnClickListener {
            val fragmentLayout = SearchFragment()
            MyLib.changeFragment(requireActivity(), fragmentLayout)
        }
    }


    private fun initInfoSinger() {
        // Nhận dữ liệu singer từ SearchFragmentSub
        val bundleReceive : Bundle = requireArguments()
        val singer : Singer = bundleReceive["object_singer_info"] as Singer

        tvSingerName.text = singer.singername
        idSinger = singer.id // get id of playlist
        Picasso.get().load(singer.image)
            .resize(800,800)
            .into(imgSinger)
    }

    private fun initTabFragment(){
        val tabOfSingerAdapter = TabOfSingerAdapter(requireActivity(), idSinger)
        viewPager.adapter = tabOfSingerAdapter
        TabLayoutMediator(tabLayout,viewPager){tittle, position ->
            when(position){
                0 ->{
                    tittle.text = "Bài hát"
                }
                1 -> {
                    tittle.text = "Album"
                }
                else -> {
                    tittle.text = "Bài hát"
                }
            }
        }.attach()
    }
}