package com.example.appnghenhaconline

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.appnghenhaconline.models.song.Song
import com.squareup.picasso.Picasso

class SongAdapter( var context: Context, var layout: Int? = null, var listSong: ArrayList<Song>) : BaseAdapter() {
    override fun getCount(): Int {
        return listSong.size
    }

    override fun getItem(position: Int): Song {
        return listSong[position]
    }

    override fun getItemId(position: Int): Long {
        return -1;
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        val viewHolder: ViewHolder
        val rowView: View
        if(view == null){
            val layoutInflater: LayoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            rowView = layoutInflater.inflate(layout!!,null)
            viewHolder = ViewHolder(rowView)
            rowView.tag = viewHolder
        }else{
            rowView = view
            viewHolder = rowView.tag as ViewHolder
        }

        val song = getItem(position)

        Picasso.get().load(song.image).into(viewHolder.imgSong)
        viewHolder.tvSongTitle.text = song.title
        return rowView
    }

    class ViewHolder(view: View){
        var imgSong: ImageView = view.findViewById(R.id.imgSong)
        var tvSongTitle: TextView = view.findViewById(R.id.titleSong)
    }
}