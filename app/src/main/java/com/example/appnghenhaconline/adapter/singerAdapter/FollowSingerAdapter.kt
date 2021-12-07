package com.example.appnghenhaconline.adapter.singerAdapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.fragment.library.Album.AlbumFromLibFragment
import com.example.appnghenhaconline.fragment.singerInfo.SingerInfoFragment
import com.example.appnghenhaconline.models.album.Album
import com.example.appnghenhaconline.models.singer.Singer
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class FollowSingerAdapter(var context: Context,
                          private var singer: ArrayList<Singer>)
    : RecyclerView.Adapter<FollowSingerAdapter.FollowSingerViewHolder>() {

    private lateinit var mListener: IonItemClickListener

    interface IonItemClickListener{
        fun onRemoveItem(position: Int)
//        fun onItemSelected(isSelected: Boolean)
    }

    fun setOnItemClickListener(listener: IonItemClickListener){
        mListener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FollowSingerViewHolder {
        val view: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.i_singer_remove_item, parent, false)
        return FollowSingerViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: FollowSingerViewHolder, position: Int) {
        holder.bindSinger(singer[position])
    }

    override fun getItemCount(): Int {
        return singer.size
    }

    class FollowSingerViewHolder(itemView: View, listener: IonItemClickListener) : RecyclerView.ViewHolder(itemView){
        var titleSinger: TextView = itemView.findViewById(R.id.titleSinger)
        var imgSinger: CircleImageView = itemView.findViewById(R.id.imgSinger)
        var layoutItem: ConstraintLayout = itemView.findViewById(R.id.layoutSinger)
        var imgRemove: ImageView = itemView.findViewById(R.id.imgRemove)

        init {
            imgRemove.setOnClickListener {
                listener.onRemoveItem(adapterPosition)
            }
        }

        fun bindSinger(itemSinger: Singer){
            titleSinger.text = itemSinger.singername
            Picasso.get().load( itemSinger.image)
                .resize(480,500)
                .placeholder(R.drawable.ic_loading_double)
                .error(R.drawable.img_error)
                .into(imgSinger)
            //Thêm sự kiện onClick
            layoutItem.setOnClickListener {v->
                val activity = v.context as AppCompatActivity
                val layoutFragment = SingerInfoFragment()

                val bundle = Bundle()
                bundle.putSerializable("object_singer_info", itemSinger)
                layoutFragment.arguments = bundle

                MyLib.changeFragment(activity, layoutFragment)
            }
        }
    }
}