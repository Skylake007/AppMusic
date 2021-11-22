package com.example.appnghenhaconline.adapter

import android.content.Context
import android.content.Intent
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
import com.example.appnghenhaconline.fragment.Library.Playlist.AddPlaylistFragment
import com.example.appnghenhaconline.models.playlist.Playlist
import com.squareup.picasso.Picasso

class MyPlaylistAdapter(var context: Context,
                        private var playlists: ArrayList<Playlist>): RecyclerView.Adapter<MyPlaylistAdapter.MyPlaylistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPlaylistViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.i_playlist_sm_item, parent, false)
        return MyPlaylistViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyPlaylistViewHolder, position: Int) {
        holder.bindMyPlaylist(playlists[position])
        }

    override fun getItemCount(): Int {
        return playlists.size
    }

    class MyPlaylistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvPlaylistTitle: TextView = itemView.findViewById(R.id.tvPlaylistTittleSM)
        var imgPlaylist: ImageView = itemView.findViewById(R.id.imgPlaylistSM)
        var layoutItem: CardView = itemView.findViewById(R.id.layoutPlaylistSM)

        fun bindMyPlaylist(itemPlaylist: Playlist){
            tvPlaylistTitle.text = itemPlaylist.playlistname

            Picasso.get().load(itemPlaylist.image).resize(480,500)
                                                    .placeholder(R.drawable.img_loading)
                                                    .error(R.drawable.img_error)
                                                    .into(imgPlaylist)

            layoutItem.setOnClickListener {v->
                val activity = v.context as AppCompatActivity
                val addPlaylistFragment = AddPlaylistFragment()

                val bundle = Bundle()
                bundle.putSerializable("object_my_playlist", itemPlaylist)
                addPlaylistFragment.arguments = bundle

                activity.supportFragmentManager.beginTransaction()
                    .replace(R.id.layoutFmLibOfPlaylist, addPlaylistFragment)
                    .addToBackStack(null)
                    .commit()
            }
        }
    }
}