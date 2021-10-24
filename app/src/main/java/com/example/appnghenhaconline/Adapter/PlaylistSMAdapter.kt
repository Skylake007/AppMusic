package com.example.appnghenhaconline.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.models.playlist.Playlist
import com.squareup.picasso.Picasso

class PlaylistSMAdapter: RecyclerView.Adapter<PlaylistSMAdapter.PlaylistSMViewHolder>() {

    private lateinit var playlists: ArrayList<Playlist>

    fun setData(playlist: ArrayList<Playlist>){
        this.playlists = playlist
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistSMViewHolder {
        var view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.playlist_sm_item, parent, false)
        return PlaylistSMViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistSMViewHolder, position: Int) {
        var playlist: Playlist = playlists.get(position)
        if (playlist==null)  return

        holder.tvPlaylistTitle.text =playlist.getTittle()
        Picasso.get().load(playlist.getResourceId()).into(holder.imgPlaylist)
        //Thêm sự kiện onClick
        holder.layoutItem.setOnClickListener {
            onClickShowInfo(playlist)
        }
    }

    private fun onClickShowInfo(playlist: Playlist) {
        Log.e("Task", playlist.getTittle())
    }
    override fun getItemCount(): Int {
        return playlists.size
    }

    class PlaylistSMViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvPlaylistTitle: TextView = itemView.findViewById(R.id.tvPlaylistTittle)
        var imgPlaylist: ImageView = itemView.findViewById(R.id.imgPlaylist)
        var layoutItem: CardView = itemView.findViewById(R.id.layoutPlaylistSM)
    }
}