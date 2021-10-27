package com.example.appnghenhaconline.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.models.playlist.Playlist
import com.squareup.picasso.Picasso

class PlaylistSMAdapter(private var playlists: ArrayList<Playlist>): RecyclerView.Adapter<PlaylistSMAdapter.PlaylistSMViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistSMViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.playlist_sm_item, parent, false)
        return PlaylistSMViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistSMViewHolder, position: Int) {
        val playlist: Playlist = playlists[position]

        holder.tvPlaylistTitle.text =playlist.title
        Picasso.get().load(playlist.resourceId).into(holder.imgPlaylist)
        //Thêm sự kiện onClick
        holder.layoutItem.setOnClickListener {
            onClickShowInfo(playlist)
        }
    }

    private fun onClickShowInfo(playlist: Playlist) {
        Log.e("Task", playlist.title)
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