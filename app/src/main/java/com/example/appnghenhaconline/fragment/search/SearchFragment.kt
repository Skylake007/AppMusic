package com.example.appnghenhaconline.fragment.search

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
import com.example.appnghenhaconline.adapter.categoryAdapter.CategoryAdapter
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.models.playlist.Category
import com.example.appnghenhaconline.models.playlist.DataCategories
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

        rcvCategory.layoutManager = GridLayoutManager(view.context,2)
        rcvCategory.setHasFixedSize(true)
        rcvCategory.adapter = categoryAdapter

        categoryAdapter.setOnItemClickListener(object : CategoryAdapter.IonItemClickListener{
            override fun onItemClick(position: Int) {
                val fragmentLayout = PlaylistByCategoryIdFragment()

                val bundle = Bundle()
                bundle.putSerializable("id_category", listCategory[position].id)
                fragmentLayout.arguments = bundle

                MyLib.changeFragment(requireActivity(), fragmentLayout)
            }
        })

        callApiCategory(listCategory,categoryAdapter)
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

}