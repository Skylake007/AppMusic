package com.example.appnghenhaconline.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.adapter.CategoryAdapter
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.models.playlist.Category
import com.example.appnghenhaconline.models.playlist.DataPlayList
import com.example.appnghenhaconline.models.playlist.Playlist
import com.example.appnghenhaconline.models.user.DataUser
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlayNowFragment : Fragment() {
    internal lateinit var view: View
    lateinit var rcvCategory: RecyclerView

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.play_now_fragment, container, false)


        initCategoryList()
        return view
    }

    private fun initCategoryList(){
        var categoryAdapter = CategoryAdapter(view.context,getListCatagory())
        rcvCategory = view.findViewById(R.id.rcvCategory)
        rcvCategory.setHasFixedSize(true)
        rcvCategory.layoutManager = LinearLayoutManager(view.context,
            LinearLayoutManager.VERTICAL,false)
        rcvCategory.adapter = categoryAdapter
    }

    private fun getListCatagory(): ArrayList<Category>{
        var listCatagory: ArrayList<Category> = ArrayList()

        var listPlaylist: ArrayList<Playlist> = ArrayList()

//        listPlaylist.add(Playlist("Phan Mạnh Quỳnh1",R.drawable.cv_img1))
//        listPlaylist.add(Playlist("Mr. Siro2",R.drawable.cv_img2))
//        listPlaylist.add(Playlist("Trịnh Thăng Bình3",R.drawable.cv_img3))
//
//        listPlaylist.add(Playlist("Phan Mạnh Quỳnh4",R.drawable.cv_img1))
//        listPlaylist.add(Playlist("Mr. Siro5",R.drawable.cv_img2))
//        listPlaylist.add(Playlist("Trịnh Thăng Bình6",R.drawable.cv_img3))

        listCatagory.add(Category(CategoryAdapter.TYPE_PLAYLIST_SL,"Play List"))
//        listCatagory.add(Category(CategoryAdapter.TYPE_PLAYLIST_SM,"Category_2",listPlaylist))
//        listCatagory.add(Category(CategoryAdapter.TYPE_PLAYLIST_SM,"Category_3",listPlaylist))
//        listCatagory.add(Category(CategoryAdapter.TYPE_PLAYLIST_SL,"Category_4",listPlaylist))

        return listCatagory
    }

    private fun callApiPlayList() {
        ApiService.apiService.getPlayList().enqueue(object : Callback<DataPlayList?> {
            override fun onResponse(call: Call<DataPlayList?>, response: Response<DataPlayList?>) {
                var dataPlayList = response.body()
                if(dataPlayList != null) {
                    if(!dataPlayList.error) {
                        val listPlayList : ArrayList<Playlist> = dataPlayList.listPlayList

                    }
                }
            }

            override fun onFailure(call: Call<DataPlayList?>, t: Throwable) {
                MyLib.showToast(requireContext(),"Call Api Error")
            }

        })
    }
}