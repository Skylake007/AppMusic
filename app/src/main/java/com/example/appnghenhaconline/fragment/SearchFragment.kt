package com.example.appnghenhaconline.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.adapter.CategoryAdapter
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.models.playlist.Category
import com.example.appnghenhaconline.models.playlist.DataCategories
import com.example.appnghenhaconline.models.playlist.DataPlayList
import com.example.appnghenhaconline.models.playlist.Playlist
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchFragment : Fragment() {
    internal lateinit var view: View
    private lateinit var tvSearch: TextView
    lateinit var rcvCategory: RecyclerView
    lateinit var listCategory : ArrayList<Category>
    lateinit var categoryAdapter : CategoryAdapter

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fm_search_fragment, container, false)

        init()

        initCategory()

        return view
    }

    private fun init(){
        tvSearch = view.findViewById(R.id.tvSearch)
        rcvCategory = view.findViewById(R.id.rcvPlaylistSearch)
        event()
    }

    private fun event(){
        tvSearch.setOnClickListener {
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainer, SearchFragmentSub())
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }
    }

    //khởi tạo danh sách chủ đề
    private fun initCategory() {
        listCategory = ArrayList()
        categoryAdapter = CategoryAdapter(view.context,listCategory)

        createCategory(rcvCategory, categoryAdapter)
        callApiCategory(listCategory,categoryAdapter)
    }

    private fun createCategory(rcv: RecyclerView, adapter: CategoryAdapter){
        rcv.layoutManager = GridLayoutManager(view.context,2)
        rcv.setHasFixedSize(true)
        rcv.adapter = adapter

        adapter.setOnItemClickListener(object :CategoryAdapter.IonItemClickListener{
            override fun onItemClick(position: Int) {
//                TODO("Not yet implemented")
            }

        })
    }

    //Get all category
    private fun callApiCategory(list : ArrayList<Category>, adapter : CategoryAdapter) {
        ApiService.apiService.getListCategories().enqueue(object : Callback<DataCategories?> {
            override fun onResponse(call: Call<DataCategories?>, response: Response<DataCategories?>) {
                var dataCategory = response.body()
                if(dataCategory != null) {
                    if(!dataCategory.error) {
                        list.addAll(dataCategory.listCategory)
                        adapter.notifyDataSetChanged()
                    }
                    else {
                        MyLib.showLog("SearchFragment.kt: " + dataCategory.message)
                    }
                }
            }

            override fun onFailure(call: Call<DataCategories?>, t: Throwable) {
                MyLib.showToast(requireContext(),"Call Api Error")
            }
        })
    }

    // hàm này sử lý sau sự kiện click vào category lấy categoryID sang Fragment mới chạy
    private fun callApiGetPlaylistByCategoryID(categoryId : String) {
        ApiService.apiService.getPlaylistByCategoryID(categoryId).enqueue(object : Callback<DataPlayList?> {
            override fun onResponse(call: Call<DataPlayList?>, response: Response<DataPlayList?>) {
                var dataPlaylist = response.body()
                if(dataPlaylist != null) {
                    if(!dataPlaylist.error) {
                        // Chỗ Nãy nghĩa lấy list playlist với adaptor xuất lên nhé
                    }
                    else {
                        MyLib.showLog("SearchFragment.kt: " + dataPlaylist.message)
                    }
                }
            }

            override fun onFailure(call: Call<DataPlayList?>, t: Throwable) {
                MyLib.showToast(requireContext(),"Call Api Error")
            }
        })
    }
}