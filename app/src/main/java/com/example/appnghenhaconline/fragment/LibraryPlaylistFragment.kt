package com.example.appnghenhaconline.fragment

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.WindowManager.LayoutParams.MATCH_PARENT
import android.view.WindowManager.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.activity.AddPlaylistActivity
import com.example.appnghenhaconline.adapter.FollowPlaylistAdapter
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.models.playlist.DataPlayList
import com.example.appnghenhaconline.models.playlist.Playlist
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LibraryPlaylistFragment: Fragment() {
    internal lateinit var view: View
    private lateinit var btnAddPlaylist: ImageView
    lateinit var listFollowPlaylist: ArrayList<Playlist>
    lateinit var rcvFollowPlaylist: RecyclerView
    lateinit var followPlaylistAdapter: FollowPlaylistAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fm_library_playlist_fragment, container, false)
        init()
        return view
    }

    private fun init(){
        btnAddPlaylist = view.findViewById(R.id.btnAddPlaylist)
        event()
        initFollowPlaylist()
    }

    private fun event(){
        btnAddPlaylist.setOnClickListener {
            openDialogAddPlaylist(Gravity.CENTER)
        }
    }

    private fun initFollowPlaylist(){
        listFollowPlaylist = ArrayList()
        followPlaylistAdapter = FollowPlaylistAdapter(view.context, listFollowPlaylist)

        rcvFollowPlaylist =view.findViewById(R.id.rcvFollowPlaylist)
        rcvFollowPlaylist.setHasFixedSize(true)
        rcvFollowPlaylist.layoutManager = LinearLayoutManager(view.context,
                    LinearLayoutManager.HORIZONTAL, false)
        rcvFollowPlaylist.adapter = followPlaylistAdapter

        callApiPlayListSM(listFollowPlaylist, followPlaylistAdapter)
    }

    private fun callApiPlayListSM(list : ArrayList<Playlist>, adapter : FollowPlaylistAdapter) {
        ApiService.apiService.getPlayList().enqueue(object : Callback<DataPlayList?> {
            override fun onResponse(call: Call<DataPlayList?>, response: Response<DataPlayList?>) {
                var dataPlayList = response.body()
                if(dataPlayList != null) {
                    if(!dataPlayList.error) {
                        list.addAll(dataPlayList.listPlayList)
                        adapter.notifyDataSetChanged()
                    }
                    else {
                        MyLib.showLog("PlayNowFragment.kt: " + dataPlayList.message)
                    }
                }
            }

            override fun onFailure(call: Call<DataPlayList?>, t: Throwable) {
                MyLib.showToast(requireContext(),"Call Api Error")
            }
        })
    }

//    hàm hiện dialog
    private fun openDialogAddPlaylist(gravity: Int){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dlg_add_playlist_dialog)
        dialog.setCancelable(true)

        val window = dialog.window
        window?.setLayout(MATCH_PARENT, WRAP_CONTENT)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val windowAttributes: WindowManager.LayoutParams = window!!.attributes
        windowAttributes.gravity = gravity
        window.attributes = windowAttributes

        val btnExit: Button = dialog.findViewById(R.id.btnExit)
        val btnAccept: Button = dialog.findViewById(R.id.btnAccept)

        btnExit.setOnClickListener {
            dialog.dismiss()
        }

        btnAccept.setOnClickListener {
            val intent = Intent(requireContext(),AddPlaylistActivity::class.java)
            startActivity(intent)
        }
        dialog.show()
    }
}