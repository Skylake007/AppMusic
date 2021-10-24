package com.example.appnghenhaconline.Fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.Adapter.CategoryAdapter
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.models.playlist.Category
import com.example.appnghenhaconline.models.playlist.Playlist
import kotlinx.android.synthetic.main.play_now_fragment.*

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
        var categoryAdapter = CategoryAdapter(view.context)
        rcvCategory = view.findViewById(R.id.rcvCategory)
        rcvCategory.setHasFixedSize(true)
        rcvCategory.layoutManager = LinearLayoutManager(view.context,
            LinearLayoutManager.VERTICAL,false)
        categoryAdapter.setData(getListCatagory())

        rcvCategory.adapter = categoryAdapter
    }

    private fun getListCatagory(): ArrayList<Category>{
        var listCatagory: ArrayList<Category> = ArrayList()

        var listPlaylist: ArrayList<Playlist> = ArrayList()
        listPlaylist.add(Playlist("Phan Mạnh Quỳnh1",R.drawable.cv_img1))
        listPlaylist.add(Playlist("Mr. Siro2",R.drawable.cv_img2))
        listPlaylist.add(Playlist("Trịnh Thăng Bình3",R.drawable.cv_img3))

        listPlaylist.add(Playlist("Phan Mạnh Quỳnh4",R.drawable.cv_img1))
        listPlaylist.add(Playlist("Mr. Siro5",R.drawable.cv_img2))
        listPlaylist.add(Playlist("Trịnh Thăng Bình6",R.drawable.cv_img3))

        listCatagory.add(Category(CategoryAdapter.TYPE_PLAYLIST_SL,"Category_1",listPlaylist))
        listCatagory.add(Category(CategoryAdapter.TYPE_PLAYLIST_SM,"Category_2",listPlaylist))
        listCatagory.add(Category(CategoryAdapter.TYPE_PLAYLIST_SM,"Category_3",listPlaylist))
        listCatagory.add(Category(CategoryAdapter.TYPE_PLAYLIST_SL,"Category_4",listPlaylist))

        return listCatagory
    }
}