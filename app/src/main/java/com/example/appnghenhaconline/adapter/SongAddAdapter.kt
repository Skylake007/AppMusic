package com.example.appnghenhaconline.adapter

import android.content.Context
import android.opengl.Visibility
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.models.song.Song
import com.squareup.picasso.Picasso

class SongAddAdapter(var context: Context,
                     private var listSong: ArrayList<Song>) : RecyclerView.Adapter<SongAddAdapter.SongNViewHolder>(){

    private lateinit var mListener: IonItemClickListener

    interface IonItemClickListener{
        fun onItemClick(position: Int)
//        fun onItemSelected(isSelected: Boolean)
    }

    fun setOnItemClickListener(listener: IonItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongNViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.i_song_add_item, parent, false)
        return SongNViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: SongNViewHolder, position: Int) {
        holder.bindSong(listSong[position])
    }

    override fun getItemCount(): Int {
       return listSong.size
    }

//    fun getSelectedSong(): ArrayList<Song>{
//        val selectedSongs: ArrayList<Song> = ArrayList()
//        for (songs in listSong){
//            if (songs.isSelected){
//                selectedSongs.add(songs)
//            }
//        }
//        return selectedSongs
//    }

    inner class SongNViewHolder(itemView: View, listener: IonItemClickListener) : RecyclerView.ViewHolder(itemView){
        var tvTittle: TextView = itemView.findViewById(R.id.titleSong)
        var imgSong: ImageView = itemView.findViewById(R.id.imgSong)
        var tvSinger : TextView = itemView.findViewById(R.id.titleSinger)
//        var viewBackground : View = itemView.findViewById(R.id.viewBackground)
        var imgSelected : ImageView = itemView.findViewById(R.id.imgSelected)
//        var layoutSong: ConstraintLayout = itemView.findViewById(R.id.layoutSong)

        init {
            imgSelected.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
        fun bindSong(itemSong: Song){
            val singerName = StringBuffer()
            for(i in itemSong.singer) {
                singerName.append(i.singername)
            }

            tvTittle.text = itemSong.title
            tvSinger.text = singerName
            Picasso.get().load(itemSong.image)
                .into(imgSong)
        }
    }
}