package com.example.appnghenhaconline.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.models.song.Song
import com.example.appnghenhaconline.models.songN.SongN
import com.squareup.picasso.Picasso

class SongNAdapter(var context: Context,
                   private var listSong: ArrayList<Song>) : RecyclerView.Adapter<SongNAdapter.SongNViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongNViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.i_song_item, parent, false)
        return SongNViewHolder(view)
    }

    override fun onBindViewHolder(holder: SongNViewHolder, position: Int) {
        val song: Song = listSong[position]

        holder.tvTittle.text = song.title
        Picasso.get().load(song.image).into(holder.imgSong)
    }

    override fun getItemCount(): Int {
       return listSong.size
    }

    class SongNViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvTittle: TextView = itemView.findViewById(R.id.titleSong)
        var imgSong: ImageView = itemView.findViewById(R.id.imgSong)
    }
}