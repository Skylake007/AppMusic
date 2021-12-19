package com.example.appnghenhaconline.adapter.playlistAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.models.playlist.PlayListUser

// adapter danh sách các playlist được tạo bởi user
class PlaylistSelectedAdapter(var context: Context,
                              private var listPlaylistSelected: ArrayList<PlayListUser>)
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
        var imgSelected : ImageView = itemView.findViewById(R.id.imgSelected)

        init {
            imgSelected.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }

        fun bindSong(itemPlaylist: PlayListUser){
            tvTittle.text = itemPlaylist.playlistName
        }
    }
}