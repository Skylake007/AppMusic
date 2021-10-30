package com.example.appnghenhaconline.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.adapter.SongNAdapter
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.models.playlist.Playlist
import com.example.appnghenhaconline.models.songN.SongN
import com.squareup.picasso.Picasso

class AlbumFragment: Fragment() {

    internal lateinit var view: View
    lateinit var rcvSong: RecyclerView
    lateinit var tittleAlbum : TextView
    lateinit var imgAlbum : ImageView

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fm_album_music_fragment, container, false)

        tittleAlbum = view.findViewById(R.id.tittleAlbumMusic)
        imgAlbum = view.findViewById(R.id.imgAlbumMusic)

        initSongList()
        initAlbum()
        return view
    }

    private fun initSongList(){
        var songNAdapter = SongNAdapter(view.context,getSongList())
        rcvSong = view.findViewById(R.id.rcvSong)
        rcvSong.setHasFixedSize(true)
        rcvSong.layoutManager = LinearLayoutManager(view.context,
            LinearLayoutManager.VERTICAL,false)
        rcvSong.adapter = songNAdapter
    }

    private fun getSongList(): ArrayList<SongN>{
        var listSong: ArrayList<SongN> = ArrayList()

        listSong.add(SongN("Cháu lên bar",R.drawable.cv_img1))
        listSong.add(SongN("Ba thương cô",R.drawable.cv_img2))
        listSong.add(SongN("1234",R.drawable.cv_img3))
        listSong.add(SongN("Alibaba",R.drawable.cv_img2))
        listSong.add(SongN("Nhạt",R.drawable.cv_img1))
        listSong.add(SongN("Người tôi yêu",R.drawable.cv_img3))
        listSong.add(SongN("Cháu lên bar",R.drawable.cv_img2))
        listSong.add(SongN("Cháu lên bar",R.drawable.cv_img1))
        listSong.add(SongN("Ba thương cô",R.drawable.cv_img2))
        listSong.add(SongN("1234",R.drawable.cv_img3))
        listSong.add(SongN("Alibaba",R.drawable.cv_img2))
        listSong.add(SongN("Nhạt",R.drawable.cv_img1))
        listSong.add(SongN("Người tôi yêu",R.drawable.cv_img3))
        listSong.add(SongN("Cháu lên bar",R.drawable.cv_img2))
        return listSong
    }

    private fun initAlbum(){  // khi nhấn vào item
        var bundleReceive : Bundle = requireArguments()
        if (bundleReceive!= null){
            var playlist : Playlist = bundleReceive["object_song"] as Playlist
            if (playlist != null){
                tittleAlbum.text = playlist.playlistName
                Picasso.get().load(playlist.image).into(imgAlbum)
            }
        }

    }
}