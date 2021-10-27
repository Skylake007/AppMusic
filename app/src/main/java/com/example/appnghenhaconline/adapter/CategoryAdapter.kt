package com.example.appnghenhaconline.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.models.playlist.Category

class CategoryAdapter(var context: Context,
                      private var listCategory: ArrayList<Category>)
                        : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){


    //biến type
    companion object{
        const val TYPE_PLAYLIST_SL: Int = 1
        const val TYPE_PLAYLIST_SM: Int = 2
    }

    override fun getItemViewType(position: Int): Int {
        return listCategory[position].type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category: Category = listCategory[position]

        //nếu type == Sl => show SLAdapter
        if (TYPE_PLAYLIST_SL == holder.itemViewType){
            val linearLayoutManager = LinearLayoutManager(context,
                                    RecyclerView.HORIZONTAL, false)
            holder.rcvPlaylist.layoutManager = linearLayoutManager

            val playlistSlAdapter = PlaylistSLAdapter(context,category.playlists)

            holder.rcvPlaylist.isFocusable = false
            holder.rcvPlaylist.adapter = playlistSlAdapter
            holder.tvNameCategory.text = category.nameCategory
        }else
            //nếu type == SM => show SMAdapter
            if (TYPE_PLAYLIST_SM == holder.itemViewType){
            val linearLayoutManager = LinearLayoutManager(context,
                                    RecyclerView.HORIZONTAL, false)
            holder.rcvPlaylist.layoutManager = linearLayoutManager

            val playlistSmAdapter = PlaylistSMAdapter(category.playlists)

            holder.rcvPlaylist.isFocusable = false
            holder.rcvPlaylist.adapter = playlistSmAdapter
            holder.tvNameCategory.text = category.nameCategory
        }
        //Thêm sự kiện onClick
        holder.layoutItem.setOnClickListener {
            onClickShowInfo(category)
        }
    }
    private fun onClickShowInfo(category: Category){
        Log.e("Task",category.nameCategory)
    }

    override fun getItemCount(): Int {
        return listCategory.size
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvNameCategory: TextView = itemView.findViewById(R.id.tvCategory)
        var rcvPlaylist: RecyclerView = itemView.findViewById(R.id.rcvPlaylist)
        var layoutItem: LinearLayout = itemView.findViewById(R.id.layoutCategory)
    }
}