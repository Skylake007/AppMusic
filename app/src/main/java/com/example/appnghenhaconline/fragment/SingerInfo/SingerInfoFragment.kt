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
import com.airbnb.lottie.LottieAnimationView
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.dataLocalManager.SharedPreferences.SessionUser
import com.example.appnghenhaconline.models.user.DataUser
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class SingerInfoFragment: Fragment() {
    internal lateinit var view: View
    private lateinit var tvSingerName: TextView
    private lateinit var imgSinger: ImageView
    private lateinit var idSinger: String
    private lateinit var btnBack: ImageView
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var sessionUser : SessionUser
    private lateinit var lavFollow : LottieAnimationView
    private var isFollow: Boolean = false

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
        lavFollow = view.findViewById(R.id.lavFollow)
        sessionUser = SessionUser(this.requireContext())
        initInfoSinger()
        initTabFragment()
        checkFollowed(idSinger)
        event()
    }

    private fun event() {
        btnBack.setOnClickListener {
            val fragmentLayout = SearchFragment()
            MyLib.changeFragment(requireActivity(), fragmentLayout)
        }
        clickFollow(idSinger)
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
        MyLib.showLog("Cái nồi này ra đc ko cu: $idSinger")
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

    // kiểm tra follow
    private fun checkFollowed(id : String) {
        val getUser = sessionUser.getUserDetails()
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        val singer : ArrayList<String> = gson.fromJson(getUser[sessionUser.KEY_SINGER],type)

        for (i in singer) {
            if (i == id) {
                isFollow = true
                MyLib.showLog(i)
                lavFollow.setMinAndMaxProgress(1f, 1f)
                lavFollow.speed = 1f
                lavFollow.playAnimation()
            }
        }
    }

    // sự kiện click để follow
    private fun clickFollow(id : String) {
        val getUser = sessionUser.getUserDetails()
        lavFollow.setOnClickListener {

            if (!isFollow){  // đang không follow
                isFollow = true
                lavFollow.setMinAndMaxProgress(0.4f, 1f)
                lavFollow.speed = 1f
                lavFollow.playAnimation()
//                btnFollow.setImageResource(R.drawable.ic_favorite_selected)
                callApiFollowSinger(id, getUser[sessionUser.KEY_ID]!!)

            }

            else{ // đang follow
                isFollow = false
                lavFollow.setMinAndMaxProgress(0f, 0.4f)
                lavFollow.speed = -1f
                lavFollow.playAnimation()
//                btnFollow.setImageResource(R.drawable.ic_favorite)
                callApiUnFollowSinger(id, getUser[sessionUser.KEY_ID]!!)
            }
        }
    }

    private fun callApiFollowSinger(singerId : String, userId : String ) {
        ApiService.apiService.followSinger(singerId,userId).enqueue(object : Callback<DataUser?> {
            override fun onResponse(call: Call<DataUser?>, response: Response<DataUser?>) {
                val dataUser = response.body()
                MyLib.showLog(dataUser.toString())
                if(dataUser!=null){
                    if(!dataUser.error){
                        MyLib.showToast(requireContext(),dataUser.message)
                        val gson = Gson()
                        val listSinger = gson.toJson(dataUser.user.favoriteSinger)
                        sessionUser.editor.putString(sessionUser.KEY_SINGER,listSinger)
                        sessionUser.editor.commit()
                    }else {
                        MyLib.showLog(dataUser.message)
                        MyLib.showToast(requireContext(),dataUser.message)
                    }
                }
            }

            override fun onFailure(call: Call<DataUser?>, t: Throwable) {
                MyLib.showLog(t.toString())
            }
        })
    }

    private fun callApiUnFollowSinger(singerId : String, userId : String ) {
        ApiService.apiService.unFollowSinger(singerId,userId).enqueue(object : Callback<DataUser?> {
            override fun onResponse(call: Call<DataUser?>, response: Response<DataUser?>) {
                val dataUser = response.body()
                MyLib.showLog(dataUser.toString())
                if(dataUser!=null){
                    if(!dataUser.error){
                        MyLib.showToast(requireContext(),dataUser.message)
                        val gson = Gson()
                        val listSinger = gson.toJson(dataUser.user.favoriteSinger)
                        sessionUser.editor.putString(sessionUser.KEY_SINGER,listSinger)
                        sessionUser.editor.commit()
                    }
                    else {
                        MyLib.showToast(requireContext(),dataUser.message)
                        MyLib.showLog(dataUser.message)
                    }
                }
            }

            override fun onFailure(call: Call<DataUser?>, t: Throwable) {
                MyLib.showLog(t.toString())
            }
        })
    }
}