package com.example.appnghenhaconline.fragment.Library.Playlist

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.view.WindowManager.LayoutParams.MATCH_PARENT
import android.view.WindowManager.LayoutParams.WRAP_CONTENT
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.adapter.FollowPlaylistAdapter
import com.example.appnghenhaconline.adapter.MyPlaylistAdapter
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.dataLocalManager.SharedPreferences.SessionUser
import com.example.appnghenhaconline.models.playlist.DataPlayList
import com.example.appnghenhaconline.models.playlist.Playlist
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class LibraryPlaylistFragment: Fragment() {
    internal lateinit var view: View
    private lateinit var btnAddPlaylist: ImageView
    private lateinit var listFollowPlaylist: ArrayList<Playlist>
    private lateinit var listMyPlaylist: ArrayList<Playlist>
    private lateinit var rcvFollowPlaylist: RecyclerView
    private lateinit var rcvMyPlaylist: RecyclerView
    private lateinit var followPlaylistAdapter: FollowPlaylistAdapter
    private lateinit var myPlaylistAdapter: MyPlaylistAdapter
    private lateinit var session: SessionUser
    private lateinit var mPlaylist: Playlist


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fm_library_playlist_fragment, container, false)

        return view
    }

    override fun onStart() {
        super.onStart()
        session = SessionUser(this.requireContext())
        init()
        event()
    }

    private fun init(){
        btnAddPlaylist = view.findViewById(R.id.btnAddPlaylist)
        initFollowPlaylist()
        initMyPlaylist()
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

        showFollowPlaylist(listFollowPlaylist, followPlaylistAdapter)
    }

    private fun initMyPlaylist(){
        listMyPlaylist = ArrayList()
        myPlaylistAdapter = MyPlaylistAdapter(view.context, listMyPlaylist)

        rcvMyPlaylist =view.findViewById(R.id.rcvMyPlaylist)
        rcvMyPlaylist.setHasFixedSize(true)
        rcvMyPlaylist.layoutManager = LinearLayoutManager(view.context,
            LinearLayoutManager.HORIZONTAL, false)
        rcvMyPlaylist.adapter = myPlaylistAdapter
        callApiPlayList(listMyPlaylist, myPlaylistAdapter)

    }

    private fun callApiPlayList(list : ArrayList<Playlist>, adapter : MyPlaylistAdapter) {
        ApiService.apiService.getPlayList().enqueue(object : Callback<DataPlayList?> {
            override fun onResponse(call: Call<DataPlayList?>, response: Response<DataPlayList?>) {
                val dataPlayList = response.body()
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

    private fun showFollowPlaylist(list : ArrayList<Playlist>, adapter : FollowPlaylistAdapter) {
        val getPlaylist = session.getUserDetails()
        val gson = Gson()
        val type: Type = object : TypeToken<ArrayList<Playlist?>?>() {}.type
        val playlistFollow : ArrayList<Playlist> = gson.fromJson(getPlaylist[session.KEY_PLAYLIST],type)
        list.addAll(playlistFollow)
        adapter.notifyDataSetChanged()
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

        val tvNamePlaylist: TextInputEditText = dialog.findViewById(R.id.edtNamePlaylist)
        val btnExit: Button = dialog.findViewById(R.id.btnExit)
        val btnAccept: Button = dialog.findViewById(R.id.btnAccept)

        btnExit.setOnClickListener {
            dialog.dismiss()
        }

        btnAccept.setOnClickListener {
            val fragmentLayout = AddPlaylistFragment()

            val bundle = Bundle()
            bundle.putSerializable("_name_playlist", tvNamePlaylist.text.toString())
            fragmentLayout.arguments = bundle

            MyLib.changeFragment(requireActivity(), fragmentLayout)
            dialog.dismiss()
        }
        dialog.show()
    }
}