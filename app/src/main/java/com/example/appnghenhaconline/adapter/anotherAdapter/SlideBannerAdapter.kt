package com.example.appnghenhaconline.adapter.anotherAdapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.models.playlist.Playlist
import com.squareup.picasso.Picasso

class SlideBannerAdapter(var context: Context,
                         private var playlist: ArrayList<Playlist>)
        : RecyclerView.Adapter<SlideBannerAdapter.SlideBannerViewHolder>() {

    internal lateinit var view: View
    private val limit: Int = 4
    lateinit var mListener: IonItemClickListener

    interface IonItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: IonItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SlideBannerViewHolder {
        view = LayoutInflater.from(parent.context)
            .inflate(R.layout.i_slide_banner_item, parent, false)

        return SlideBannerViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: SlideBannerViewHolder, position: Int) {
        val playlist: Playlist = playlist[position]

        Picasso.get().load(playlist.image)
                        .placeholder(R.drawable.ic_loading_double)
                        .error(R.drawable.img_error)
                        .into(holder.imgBanner)

        holder.tvBanner.text = playlist.playlistname
    }

    override fun getItemCount(): Int {
        if (playlist.size > limit){
            return limit
        }else{
            return playlist.size
        }
    }

    class SlideBannerViewHolder(itemView: View, listener: IonItemClickListener)
                                : RecyclerView.ViewHolder(itemView){

        var imgBanner: ImageView = itemView.findViewById(R.id.imgBanner)
        var tvBanner: TextView = itemView.findViewById(R.id.tvPlByCategory)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }
}