package com.example.appnghenhaconline.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.size
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.activity.PlayMusicActivity
import com.example.appnghenhaconline.adapter.*
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.dataLocalManager.Service.MyService
import com.example.appnghenhaconline.models.album.Album
import com.example.appnghenhaconline.models.album.DataAlbum
import com.example.appnghenhaconline.models.playlist.Category
import com.example.appnghenhaconline.models.playlist.DataCategories
import com.example.appnghenhaconline.models.playlist.DataPlayList
import com.example.appnghenhaconline.models.playlist.Playlist
import me.relex.circleindicator.CircleIndicator
import me.relex.circleindicator.CircleIndicator3
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.abs

class PlayNowFragment : Fragment() {
    internal lateinit var view: View
    private lateinit var rcvCategory: RecyclerView
    private lateinit var listPlaylist : ArrayList<Playlist>
    private lateinit var listAlbum : ArrayList<Album>
    private lateinit var playlistAdapterSL : PlaylistSLAdapter
    private lateinit var playlistAdapterSM : PlaylistSMAdapter
    private lateinit var albumAdapter: AlbumAdapter
    private lateinit var viewPagerBanner: ViewPager2
    private lateinit var ciBanner: CircleIndicator3
    private lateinit var bannerAdapter: SlideBannerAdapter
    private lateinit var listBanner: ArrayList<Playlist>
    private var timer = Timer()

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fm_play_now_fragment, container, false)
        init()
        return view
    }

    private fun init(){
        rcvCategory = view.findViewById(R.id.rcvCategory1)
        viewPagerBanner = view.findViewById(R.id.viewPagerBanner)
        ciBanner = view.findViewById(R.id.ciBanner)

        initCategoryList()
        initBanner()
    }
    //region INIT ADAPTER

    private fun initCategoryList(){
        //Khởi tạo danh sách playlist
        //Playlist_1
        listPlaylist = ArrayList()
        playlistAdapterSL = PlaylistSLAdapter(view.context,listPlaylist)

        createCategorySL(rcvCategory, playlistAdapterSL)
        callApiPlayList(listPlaylist,playlistAdapterSL)

        //Playlist_2
        listPlaylist = ArrayList()
        playlistAdapterSM = PlaylistSMAdapter(view.context,listPlaylist)
        rcvCategory = view.findViewById(R.id.rcvCategory2)

        createCategorySM(rcvCategory, playlistAdapterSM)
        callApiPlayListSM(listPlaylist,playlistAdapterSM)

        //Album_1
        listAlbum = ArrayList()
        albumAdapter = AlbumAdapter(view.context,listAlbum)
        rcvCategory = view.findViewById(R.id.rcvCategory3)

        createAlbumSM(rcvCategory, albumAdapter)
        callApiAlbum(listAlbum,albumAdapter)

        //Album_2
        listAlbum = ArrayList()
        albumAdapter = AlbumAdapter(view.context,listAlbum)
        rcvCategory = view.findViewById(R.id.rcvCategory4)

        createAlbumSM(rcvCategory, albumAdapter)
        callApiAlbum(listAlbum,albumAdapter)
    }
    //endregion
    //===========================================================
    //region ANOTHER FUNCTION

    private fun initBanner(){
        listBanner = ArrayList()
        bannerAdapter = SlideBannerAdapter(requireContext(), listBanner)
        viewPagerBanner.adapter = bannerAdapter
        viewPagerBanner.clipToPadding = false
        viewPagerBanner.clipChildren = false
        viewPagerBanner.offscreenPageLimit = 3
        viewPagerBanner.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(20))
        compositePageTransformer.addTransformer { page, position ->
            val r : Float = 1 - abs(position)
            page.scaleY = 0.8f + r * 0.2f
        }

        viewPagerBanner.setPageTransformer(compositePageTransformer)
        bannerAdapter.setOnItemClickListener(object : SlideBannerAdapter.IonItemClickListener{
            override fun onItemClick(position: Int) {
                val layoutFragment = ListSongFragment()

                val bundle = Bundle()
                bundle.putSerializable("object_song", listBanner[position])
                layoutFragment.arguments = bundle

                MyLib.changeFragment(requireActivity(), layoutFragment)
            }
        })
//        ciBanner.setViewPager(viewPagerBanner)
//        bannerAdapter.registerDataSetObserver(ciBanner.dataSetObserver)
        ciBanner.setViewPager(viewPagerBanner)

        bannerAdapter.registerAdapterDataObserver(ciBanner.adapterDataObserver)

        callApiBanner(listBanner, bannerAdapter)
        autoSlideBanner()
    }

    private fun autoSlideBanner(){
        timer.schedule(object :TimerTask(){
            override fun run() {
                Handler(Looper.getMainLooper()).post {
                    var currentItem = viewPagerBanner.currentItem
                    val totalItem = 3
                    if (currentItem < totalItem) {
                        currentItem++
                        viewPagerBanner.currentItem = currentItem
                    } else {
                        viewPagerBanner.currentItem = 0
                    }
                }
            }
        },5000,5000)
    }

//    private fun callApiCategory(list : ArrayList<Category>, adapter : SlideBannerAdapter) {
//        ApiService.apiService.getListCategories().enqueue(object : Callback<DataCategories?> {
//            override fun onResponse(call: Call<DataCategories?>, response: Response<DataCategories?>) {
//                var dataCategory = response.body()
//                if(dataCategory != null) {
//                    if(!dataCategory.error) {
//
//                        list.addAll(dataCategory.listCategory)
//                        adapter.notifyDataSetChanged()
//                    }
//                    else {
//                        MyLib.showLog("SearchFragment.kt: " + dataCategory.message)
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<DataCategories?>, t: Throwable) {
//                MyLib.showToast(requireContext(),"Call Api Error")
//            }
//        })
//    }

    private fun callApiBanner(list : ArrayList<Playlist>, adapter : SlideBannerAdapter) {
        ApiService.apiService.getPlayList().enqueue(object : Callback<DataPlayList?> {
            override fun onResponse(call: Call<DataPlayList?>, response: Response<DataPlayList?>) {
                var dataPlayList = response.body()
                if(dataPlayList != null) {
                    if(!dataPlayList.error) {
                        list.addAll(dataPlayList.listPlayList)
                        adapter.notifyDataSetChanged()
                    }
                    else {
                        MyLib.showLog("PlayNowFragment.kt: " + dataPlayList.message)
                    }
                }
            }

            override fun onFailure(call: Call<DataPlayList?>, t: Throwable) {
                MyLib.showToast(requireContext(),"Call Api Error")
            }
        })
    }

    // set adapter cho Playlist SL
    private fun createCategorySL(rcv: RecyclerView, adapter: PlaylistSLAdapter){
        rcv.layoutManager = LinearLayoutManager(view.context,
                            LinearLayoutManager.HORIZONTAL, false)
        rcv.setHasFixedSize(true)
        rcv.adapter = adapter
    }

    // set adapter cho Playlist SM
    private fun createCategorySM(rcv: RecyclerView, adapter: PlaylistSMAdapter){
        rcv.layoutManager = LinearLayoutManager(view.context,
            LinearLayoutManager.HORIZONTAL, false)
        rcv.setHasFixedSize(true)
        rcv.adapter = adapter
    }

    // set adapter cho Album
    private fun createAlbumSM(rcv: RecyclerView, adapter: AlbumAdapter){
        rcv.layoutManager = LinearLayoutManager(view.context,
            LinearLayoutManager.HORIZONTAL, false)
        rcv.setHasFixedSize(true)
        rcv.adapter = adapter
    }
    //endregion
    //===========================================================
    //region CALL API

    private fun callApiPlayList(list : ArrayList<Playlist>, adapter : PlaylistSLAdapter) {
        ApiService.apiService.getPlayList().enqueue(object : Callback<DataPlayList?> {
            override fun onResponse(call: Call<DataPlayList?>, response: Response<DataPlayList?>) {
                var dataPlayList = response.body()
                if(dataPlayList != null) {
                    if(!dataPlayList.error) {
                        list.addAll(dataPlayList.listPlayList)
                        adapter.notifyDataSetChanged()
                    }
                    else {
                        MyLib.showLog("PlayNowFragment.kt: " + dataPlayList.message)
                    }
                }
            }

            override fun onFailure(call: Call<DataPlayList?>, t: Throwable) {
                MyLib.showToast(requireContext(),"Call Api Error")
            }
        })
    }

    private fun callApiPlayListSM(list : ArrayList<Playlist>, adapter : PlaylistSMAdapter) {
        ApiService.apiService.getPlayList().enqueue(object : Callback<DataPlayList?> {
            override fun onResponse(call: Call<DataPlayList?>, response: Response<DataPlayList?>) {
                var dataPlayList = response.body()
                if(dataPlayList != null) {
                    if(!dataPlayList.error) {
                        list.addAll(dataPlayList.listPlayList)
                        adapter.notifyDataSetChanged()
                    }
                    else {
                        MyLib.showLog("PlayNowFragment.kt: " + dataPlayList.message)
                    }
                }
            }

            override fun onFailure(call: Call<DataPlayList?>, t: Throwable) {
                MyLib.showToast(requireContext(),"Call Api Error")
            }
        })
    }

    private fun callApiAlbum(list : ArrayList<Album>, adapter : AlbumAdapter) {
        ApiService.apiService.getAllAlbum().enqueue(object : Callback<DataAlbum?> {
            override fun onResponse(call: Call<DataAlbum?>, response: Response<DataAlbum?>) {
                var dataAlbum = response.body()
                if(dataAlbum != null) {
                    if (!dataAlbum.error) {
                        list.addAll(dataAlbum.albums)
                        adapter.notifyDataSetChanged()
                    }
                    else {
                        MyLib.showLog("PlayNowFragment.kt: " + dataAlbum.message)
                    }
                }
            }

            override fun onFailure(call: Call<DataAlbum?>, t: Throwable) {
                MyLib.showToast(requireContext(),"Call Api Error")
            }

        })
    }

    override fun onDestroy() {
        super.onDestroy()
        if (timer!=null){
            timer.cancel()
            timer = Timer()
        }
    }
}