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
import androidx.viewpager2.widget.ViewPager2
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.adapter.FollowPlaylistAdapter
import com.example.appnghenhaconline.adapter.MyPlaylistAdapter
import com.example.appnghenhaconline.adapter.TabOfLibPlaylistAdapter
import com.example.appnghenhaconline.adapter.TabOfSingerAdapter
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.dataLocalManager.SharedPreferences.SessionUser
import com.example.appnghenhaconline.fragment.Library.LibraryFragment
import com.example.appnghenhaconline.fragment.ListSongFragment
import com.example.appnghenhaconline.fragment.PlayNowFragment
import com.example.appnghenhaconline.models.playlist.DataPlayList
import com.example.appnghenhaconline.models.playlist.DataPlayListUser
import com.example.appnghenhaconline.models.playlist.PlayListUser
import com.example.appnghenhaconline.models.playlist.Playlist
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.android.material.textfield.TextInputEditText
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type

class LibraryPlaylistFragment: Fragment() {
    internal lateinit var view: View
    private lateinit var btnBack: ImageView
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout
    private lateinit var session: SessionUser

    companion object{
        lateinit var btnAddPlaylist: FloatingActionButton
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fm_library_playlist_fragment, container, false)

        return view
    }

    override fun onStart() {
        super.onStart()
        init()
    }

    private fun init(){
        session = SessionUser(this.requireContext())
        btnBack = view.findViewById(R.id.btnBack)
        btnAddPlaylist = view.findViewById(R.id.fabAddPlaylist)
        viewPager = view.findViewById(R.id.viewPager)
        tabLayout = view.findViewById(R.id.tabLayout)
        initTabFragment()
        event()
    }

    private fun event(){
        btnBack.setOnClickListener {
            val fragmentLayout = LibraryFragment()
            MyLib.changeFragment(requireActivity(), fragmentLayout)
        }
        btnAddPlaylist.setOnClickListener {
            openDialogAddPlaylist(Gravity.CENTER)
        }
    }

    private fun initTabFragment(){
        val tabOfSingerAdapter = TabOfLibPlaylistAdapter(requireActivity())
        viewPager.adapter = tabOfSingerAdapter

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener{
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if (tab?.position == 0){
                    btnAddPlaylist.show()
                }else{
                    btnAddPlaylist.hide()
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
//                TODO("Not yet implemented")
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
//                TODO("Not yet implemented")
            }
        })

        TabLayoutMediator(tabLayout,viewPager){tittle, position ->
            when(position){
                0 ->{
                    tittle.text = "Playlist của tôi"
                }
                1 -> {
                    tittle.text = "Playlist ưa thích"
                }
                else -> {
                    tittle.text = "Playlist của tôi"
                }
            }
        }.attach()
    }

    //  hàm hiện dialog
    private fun openDialogAddPlaylist(gravity: Int){
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dlg_add_playlist_dialog)
        dialog.setCancelable(true)

        val window = dialog.window
        window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
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
            if (tvNamePlaylist.text.toString().trim() != "") {
                var getUser = session.getUserDetails()
                callApiCreatePlaylistUser(getUser[session.KEY_ID]!!,tvNamePlaylist.text.toString())
                dialog.dismiss()
            }
            else {
                MyLib.showToast(requireContext(),"Vui Lòng Điền tên PlayList")
            }
        }
        dialog.show()
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