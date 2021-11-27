package com.example.appnghenhaconline.adapter

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.fragment.ListSongFragment
import com.example.appnghenhaconline.models.playlist.Playlist
import com.squareup.picasso.Picasso

class FollowPlaylistAdapter(var context: Context,
                            private var playlists: ArrayList<Playlist>): RecyclerView.Adapter<FollowPlaylistAdapter.FollowPlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowPlaylistViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.i_playlist_sm_item, parent, false)
        return FollowPlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowPlaylistViewHolder, position: Int) {
        val playlist: Playlist = playlists[position]

        holder.tvPlaylistTitle.text =playlist.playlistname
        Picasso.get().load(playlist.image)
                        .resize(480,500)
                        .placeholder(R.drawable.ic_loading_double)
                        .error(R.drawable.img_error)
                        .into(holder.imgPlaylist)
        //Thêm sự kiện onClick
        holder.layoutItem.setOnClickListener {v->
            val activity = v.context as AppCompatActivity
            val listSongFragment = ListSongFragment()

            val bundle = Bundle()
            bundle.putSerializable("object_song", playlist)
            listSongFragment.arguments = bundle

            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.layoutFmLibOfPlaylist, listSongFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    class FollowPlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvPlaylistTitle: TextView = itemView.findViewById(R.id.tvPlaylistTittleSM)
        var imgPlaylist: ImageView = itemView.findViewById(R.id.imgPlaylistSM)
        var layoutItem: CardView = itemView.findViewById(R.id.layoutPlaylistSM)
    }
}