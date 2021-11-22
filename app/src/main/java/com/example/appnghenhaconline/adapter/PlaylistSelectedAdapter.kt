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
import com.example.appnghenhaconline.models.playlist.Playlist
import com.example.appnghenhaconline.models.song.Song
import com.squareup.picasso.Picasso

class PlaylistSelectedAdapter(var context: Context,
                              private var listPlaylistSelected: ArrayList<Playlist>)
    : RecyclerView.Adapter<PlaylistSelectedAdapter.PlaylistSelectedViewHolder>(){

    private lateinit var mListener: IonItemClickListener

    interface IonItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: IonItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistSelectedViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.i_select_playlist_item, parent, false)
        return PlaylistSelectedViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: PlaylistSelectedViewHolder, position: Int) {
        holder.bindSong(listPlaylistSelected[position])
    }

    override fun getItemCount(): Int {
       return listPlaylistSelected.size
    }

    inner class PlaylistSelectedViewHolder(itemView: View, listener: IonItemClickListener) : RecyclerView.ViewHolder(itemView){
        var tvTittle: TextView = itemView.findViewById(R.id.titlePlaylistSelected)
        var imgSong: ImageView = itemView.findViewById(R.id.imgPlaylistSelected)
        var imgSelected : ImageView = itemView.findViewById(R.id.imgSelected)

        init {
            imgSelected.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

        fun bindSong(itemPlaylist: Playlist){
            tvTittle.text = itemPlaylist.playlistname
            Picasso.get().load(itemPlaylist.image)
                        .into(imgSong)
        }
    }
}