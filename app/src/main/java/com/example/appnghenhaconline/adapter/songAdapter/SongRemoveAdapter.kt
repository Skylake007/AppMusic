package com.example.appnghenhaconline.adapter.songAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.models.song.Song
import com.squareup.picasso.Picasso

//adapter bài hát để xóa vào playlist có phát nhạc
class SongRemoveAdapter(var context: Context,
                        private var listSong: ArrayList<Song>)
    : RecyclerView.Adapter<SongRemoveAdapter.SongRemoveViewHolder>(){

    private lateinit var mListener: IonItemClickListener

    interface IonItemClickListener{
        fun onItemClick(position: Int)
        fun onItemSelected(position: Int)
    }

    fun setOnItemClickListener(listener: IonItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongRemoveViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.i_song_remove_item, parent, false)
        return SongRemoveViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: SongRemoveViewHolder, position: Int) {
        holder.bindSong(listSong[position])
    }

    override fun getItemCount(): Int {
       return listSong.size
    }

    inner class SongRemoveViewHolder(itemView: View, listener: IonItemClickListener) : RecyclerView.ViewHolder(itemView){
        var tvTittle: TextView = itemView.findViewById(R.id.titleSong)
        var imgSong: ImageView = itemView.findViewById(R.id.imgSong)
        var tvSinger : TextView = itemView.findViewById(R.id.titleSinger)
        var imgSelected : ImageView = itemView.findViewById(R.id.imgSelected)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
            imgSelected.setOnClickListener {
                listener.onItemSelected(adapterPosition)
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