package com.example.appnghenhaconline.fragment.Library.Playlist

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.adapter.MyPlaylistAdapter
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.dataLocalManager.SharedPreferences.SessionUser
import com.example.appnghenhaconline.models.playlist.DataPlayListUser
import com.example.appnghenhaconline.models.playlist.PlayListUser
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MyPlaylistFragment : Fragment(){
    internal lateinit var view: View
    private lateinit var listMyPlaylist: ArrayList<PlayListUser>
    private lateinit var rcvMyPlaylist: RecyclerView
    private lateinit var myPlaylistAdapter: MyPlaylistAdapter
    private lateinit var session: SessionUser

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        view = inflater.inflate(R.layout.tab_my_playlist, container, false)
        init()
        return view
    }

    private fun init(){
        session = SessionUser(this.requireContext())
        initMyPlaylist()
        event()
    }

    private fun event() {
    }

    private fun initMyPlaylist(){
        listMyPlaylist = ArrayList()
        myPlaylistAdapter = MyPlaylistAdapter(view.context, listMyPlaylist)

        rcvMyPlaylist =view.findViewById(R.id.rcvMyPlaylist)
        rcvMyPlaylist.setHasFixedSize(true)
        rcvMyPlaylist.layoutManager = GridLayoutManager(view.context,2)
        rcvMyPlaylist.adapter = myPlaylistAdapter
        val getUser = session.getUserDetails()
        callApiLoadPlayListUser(listMyPlaylist, myPlaylistAdapter, getUser[session.KEY_ID].toString())

    }

    // load Playlist User created
    private fun callApiLoadPlayListUser(list : ArrayList<PlayListUser>,
                                        adapter : MyPlaylistAdapter, idUser : String) {
        ApiService.apiService.loadPlaylistUser(idUser).enqueue(object :
            Callback<DataPlayListUser?> {
            override fun onResponse(call: Call<DataPlayListUser?>, response: Response<DataPlayListUser?>) {
                val dataPlayList = response.body()
                if(dataPlayList != null) {
                    if (!dataPlayList.error) {
                        val dataPlayListUser = dataPlayList.listPlayListUser

                        list.addAll(dataPlayListUser)
                        adapter.notifyDataSetChanged()
                    }
                    else {
                        MyLib.showToast(requireContext(),dataPlayList.message)
                    }
                }
            }

            override fun onFailure(call: Call<DataPlayListUser?>, t: Throwable) {
                MyLib.showToast(requireContext(),"Call Api Error" )
            }
        })
    }

    //call Api Create playlist user
    private fun callApiCreatePlaylistUser(userId : String, playlistName : String ) {
        ApiService.apiService.createPlaylistUser(userId, playlistName).enqueue(object : Callback<DataPlayListUser?> {
            override fun onResponse(call: Call<DataPlayListUser?>, response: Response<DataPlayListUser?>) {
                val playList = response.body()
                if (playList != null) {
                    if (!playList.error) {
                        val playlist : PlayListUser = playList.playlistUser

                        val fragmentLayout = AddPlaylistFragment()

                        val bundle = Bundle()
                        bundle.putSerializable("id_playlist",playlist.id)
                        fragmentLayout.arguments = bundle

                        MyLib.changeFragment(requireActivity(), fragmentLayout)
                    }
                    else {
                        MyLib.showToast(requireContext(),playList.message)
                    }
                }
            }

            override fun onFailure(call: Call<DataPlayListUser?>, t: Throwable) {
                MyLib.showToast(requireContext(),"Call Api Error")
            }

        })
    }

}