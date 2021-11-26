package com.example.appnghenhaconline.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.models.singer.Singer
import com.example.appnghenhaconline.models.song.Song
import com.squareup.picasso.Picasso

class SingerAdapter(var context: Context,
                    private var listSong: ArrayList<Singer>) : RecyclerView.Adapter<SingerAdapter.SingerViewHolder>(){

    private lateinit var mListener: IonItemClickListener
    interface IonItemClickListener{
        fun onItemClick(position: Int)
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
        val singer: Singer = listSong[position]

        holder.singerName.text = singer.singername
        Picasso.get().load(singer.image)
            .resize(80,80)
            .into(holder.imgSinger)
    }

    override fun getItemCount(): Int {
       return listSong.size
    }

    inner class SingerViewHolder(itemView: View, listener: IonItemClickListener) : RecyclerView.ViewHolder(itemView){
        var singerName: TextView = itemView.findViewById(R.id.tvSingerName)
        var imgSinger: ImageView = itemView.findViewById(R.id.imgSinger)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}