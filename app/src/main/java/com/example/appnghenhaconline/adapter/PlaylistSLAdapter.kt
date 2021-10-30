package com.example.appnghenhaconline.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.fragment.AlbumFragment
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.models.playlist.Playlist
import com.squareup.picasso.Picasso

class PlaylistSLAdapter(var context: Context,
                        private var playlists: ArrayList<Playlist>): RecyclerView.Adapter<PlaylistSLAdapter.PlaylistSLViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistSLViewHolder {
        val view: View = LayoutInflater.from(parent.context)
                            .inflate(R.layout.i_playlist_sl_item, parent, false)
        return PlaylistSLViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistSLViewHolder, position: Int) {
        val playlist: Playlist = playlists[position]

        holder.tvPlaylistTitle.text =playlist.playlistName
        Picasso.get().load(playlist.image).into(holder.imgPlaylist)

        //Thêm sự kiện onClick
        holder.layoutItem.setOnClickListener {v->
            val activity = v.context as AppCompatActivity
            val albumFragment = AlbumFragment()

            var bundle = Bundle()
            bundle.putSerializable("object_song", playlist)
            albumFragment.arguments = bundle

            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.playNowFragmentLayout, albumFragment)
                .addToBackStack(null)
                .commit()
        }
    }

//    private fun onClickShowInfo(playlist: Playlist){
//        Log.e("Task", playlist.title)
//
//        var activity = v.con
//        var bundle = Bundle()
//        bundle.putString("_item_album_selected", "alo")
//        var albumFragment = AlbumFragment()
//        activity.supportFragmentManager.beginTransaction()
//            .replace(R.id.layoutCategory, albumFragment)
//            .addToBackStack(null)
//            .commit()
//    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    class PlaylistSLViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvPlaylistTitle: TextView = itemView.findViewById(R.id.tvPlaylistTittle)
        var imgPlaylist: ImageView = itemView.findViewById(R.id.imgPlaylist)
        var layoutItem: CardView = itemView.findViewById(R.id.layoutPlaylistSL)
    }
}