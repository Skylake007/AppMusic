package com.example.appnghenhaconline.adapter.categoryAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.models.playlist.Category
import com.squareup.picasso.Picasso

class CategoryAdapter (var context: Context,
                       private var categories: ArrayList<Category>)
    : RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>(){

    internal lateinit var view: View
    lateinit var mListener: IonItemClickListener

    interface IonItemClickListener{
        fun onClickItem(position: Int)
    }

    fun setOnItemClickListener(listener: IonItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.i_category_item, parent, false)

        return CategoryViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bindCategory(categories[position])
    }

    override fun getItemCount(): Int {
        return categories.size
    }

    class CategoryViewHolder(itemView: View, listener: IonItemClickListener) : RecyclerView.ViewHolder(itemView){
        var tvCategoryTitle: TextView = itemView.findViewById(R.id.tvCategorySearch)
        var imgCategory: ImageView = itemView.findViewById(R.id.imgCategorySearch)

        init {
            itemView.setOnClickListener {
                listener.onClickItem(adapterPosition)
            }
        }

        fun bindCategory(itemCategory: Category){
            tvCategoryTitle.text = itemCategory.categoryname

            Picasso.get().load(itemCategory.imageCategory)
                .resize(480,500)
                .placeholder(R.drawable.ic_loading_double)
                .error(R.drawable.img_error)
                .into(imgCategory)
        }
    }
}