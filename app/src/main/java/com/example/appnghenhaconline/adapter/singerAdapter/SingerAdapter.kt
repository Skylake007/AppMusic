package com.example.appnghenhaconline.adapter.singerAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.models.singer.Singer
import com.squareup.picasso.Picasso

class SingerAdapter(var context: Context,
                    private var listSinger: ArrayList<Singer>) : RecyclerView.Adapter<SingerAdapter.SingerViewHolder>(){

    private lateinit var mListener: IonItemClickListener
    interface IonItemClickListener{
        fun onClickItem(position: Int)
    }
    fun setOnItemClickListener(listener: IonItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingerViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.i_singer_item, parent, false)
        return SingerViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: SingerViewHolder, position: Int) {
        holder.bindSinger(listSinger[position])
    }

    override fun getItemCount(): Int {
       return listSinger.size
    }

    inner class SingerViewHolder(itemView: View, listener: IonItemClickListener) : RecyclerView.ViewHolder(itemView){
        var singerName: TextView = itemView.findViewById(R.id.tvSingerName)
        var imgSinger: ImageView = itemView.findViewById(R.id.imgSinger)

        init {
            itemView.setOnClickListener {
                listener.onClickItem(adapterPosition)
            }
        }

        fun bindSinger(itemSinger: Singer){
            singerName.text = itemSinger.singername
            Picasso.get().load(itemSinger.image)
                .resize(80,80)
                .into(imgSinger)
        }
    }
}