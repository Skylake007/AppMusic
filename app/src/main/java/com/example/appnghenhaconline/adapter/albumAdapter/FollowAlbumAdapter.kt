package com.example.appnghenhaconline.adapter.albumAdapter

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
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.fragment.library.Album.AlbumFromLibFragment
import com.example.appnghenhaconline.models.album.Album
import com.squareup.picasso.Picasso

class FollowAlbumAdapter(var context: Context,
                         private var albums: ArrayList<Album>)
    : RecyclerView.Adapter<FollowAlbumAdapter.FollowAlbumViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowAlbumViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.i_playlist_sm_item, parent, false)
        return FollowAlbumViewHolder(view)
    }

    override fun onBindViewHolder(holder: FollowAlbumViewHolder, position: Int) {
        holder.bindAlbum(albums[position])
    }

    override fun getItemCount(): Int {
        return albums.size
    }

    class FollowAlbumViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var tvAlbumTitle: TextView = itemView.findViewById(R.id.tvPlaylistTittleSM)
        var imgAlbum: ImageView = itemView.findViewById(R.id.imgPlaylistSM)
        var layoutItem: CardView = itemView.findViewById(R.id.layoutPlaylistSM)

        fun bindAlbum(itemAlbum: Album){
            tvAlbumTitle.text = itemAlbum.albumname
            Picasso.get().load(itemAlbum.imageAlbum)
//                .resize(480,500)
                .placeholder(R.drawable.ic_loading_double)
                .error(R.drawable.img_error)
                .into(imgAlbum)
            //Thêm sự kiện onClick
            layoutItem.setOnClickListener {v->
                val activity = v.context as AppCompatActivity
                val layoutFragment = AlbumFromLibFragment()

                val bundle = Bundle()
                bundle.putSerializable("object_album", itemAlbum)
                layoutFragment.arguments = bundle

                MyLib.changeFragment(activity, layoutFragment)
            }
        }
    }
}