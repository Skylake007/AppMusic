package com.example.appnghenhaconline.Adapter

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

class CategoryAdapter(context: Context): RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>()
                                    ,View.OnClickListener{

    private var context: Context = context
    private lateinit var listCategory: ArrayList<Category>

    //biến type
    companion object{
        const val TYPE_PLAYLIST_SL: Int = 1
        const val TYPE_PLAYLIST_SM: Int = 2
    }

    fun setData(list: ArrayList<Category>){
        this.listCategory = list
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return listCategory[position].getType()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        var category: Category = listCategory[position]
        if (category==null)  return

        //nếu type == Sl => show SLAdapter
        if (TYPE_PLAYLIST_SL == holder.itemViewType){
            var linearLayoutManager = LinearLayoutManager(context,
                                    RecyclerView.HORIZONTAL, false)
            holder.rcvPlaylist.layoutManager = linearLayoutManager

            var playlistSlAdapter = PlaylistSLAdapter()
            playlistSlAdapter.setData(category.getPlaylists())

            holder.rcvPlaylist.isFocusable = false
            holder.rcvPlaylist.adapter = playlistSlAdapter
            holder.tvNameCategory.text = category.getNameCategory()
        }else
            //nếu type == SM => show SMAdapter
            if (TYPE_PLAYLIST_SM == holder.itemViewType){
            var linearLayoutManager = LinearLayoutManager(context,
                                    RecyclerView.HORIZONTAL, false)
            holder.rcvPlaylist.layoutManager = linearLayoutManager

            var playlistSmAdapter = PlaylistSMAdapter()
            playlistSmAdapter.setData(category.getPlaylists())

            holder.rcvPlaylist.isFocusable = false
            holder.rcvPlaylist.adapter = playlistSmAdapter
            holder.tvNameCategory.text = category.getNameCategory()
        }
        //Thêm sự kiện onClick
        holder.layoutItem.setOnClickListener {
            onClickShowInfo(category)
        }
    }
    private fun onClickShowInfo(category: Category){
        Log.e("Task",category.getNameCategory())
    }

    override fun getItemCount(): Int {
        return listCategory.size
    }

    class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvNameCategory: TextView = itemView.findViewById(R.id.tvCategory)
        var rcvPlaylist: RecyclerView = itemView.findViewById(R.id.rcvPlaylist)
        var layoutItem: LinearLayout = itemView.findViewById(R.id.layoutCategory)
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }
}