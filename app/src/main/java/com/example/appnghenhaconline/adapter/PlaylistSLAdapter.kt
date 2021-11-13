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
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.fragment.ListSongFragment
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.models.playlist.Playlist
import com.squareup.picasso.Picasso


class PlaylistSLAdapter(var context: Context,
                        private var playlists: ArrayList<Playlist>)
    : RecyclerView.Adapter<PlaylistSLAdapter.PlaylistSLViewHolder>() {

    internal lateinit var view: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistSLViewHolder {
        view = LayoutInflater.from(parent.context)
                                .inflate(R.layout.i_playlist_sl_item, parent, false)

        return PlaylistSLViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaylistSLViewHolder, position: Int) {

        val playlist: Playlist = playlists[position]

        holder.tvPlaylistTitle.text =playlist.playlistname

        Picasso.get().load(playlist.image)
                        .resize(480,500)
                        .placeholder(R.drawable.img_loading)
                        .error(R.drawable.img_error)
                        .into(holder.imgPlaylist)
        //Thêm sự kiện onClick
        holder.layoutItem.setOnClickListener {v->
            val activity = v.context as AppCompatActivity
            val albumFragment = ListSongFragment()

            val bundle = Bundle()
            bundle.putSerializable("object_song", playlist)
            albumFragment.arguments = bundle

            activity.supportFragmentManager.beginTransaction()
                .replace(R.id.playNowFragmentLayout, albumFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun getItemCount(): Int {
        return playlists.size
    }

    class PlaylistSLViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvPlaylistTitle: TextView = itemView.findViewById(R.id.tvPlaylistTittleSL)
        var imgPlaylist: ImageView = itemView.findViewById(R.id.imgPlaylistSL)
        var layoutItem: CardView = itemView.findViewById(R.id.layoutPlaylistSL)
    }
}