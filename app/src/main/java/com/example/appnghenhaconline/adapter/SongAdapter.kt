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
import com.example.appnghenhaconline.models.song.Song
import com.squareup.picasso.Picasso

class SongAdapter(var context: Context,
                  private var listSong: ArrayList<Song>) : RecyclerView.Adapter<SongAdapter.SongNViewHolder>(){

    private lateinit var mListener: IonItemClickListener
    interface IonItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: IonItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongNViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.i_song_item, parent, false)
        return SongNViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: SongNViewHolder, position: Int) {
        val song: Song = listSong[position]
        var singerName = StringBuffer()
        for(i in song.singer) {
            singerName.append(i.singername)
        }


        holder.tvTittle.text = song.title
        Picasso.get().load(song.image).into(holder.imgSong)
        holder.tvSinger.text  = singerName
    }

    override fun getItemCount(): Int {
       return listSong.size
    }

    inner class SongNViewHolder(itemView: View, listener: IonItemClickListener) : RecyclerView.ViewHolder(itemView){
        var tvTittle: TextView = itemView.findViewById(R.id.titleSong)
        var imgSong: ImageView = itemView.findViewById(R.id.imgSong)
        var tvSinger : TextView = itemView.findViewById(R.id.titleSinger)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}