package com.example.appnghenhaconline.fragment.Library.Playlist

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.adapter.PlaylistSelectedAdapter
import com.example.appnghenhaconline.adapter.SongAddAdapter
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.models.playlist.DataPlayList
import com.example.appnghenhaconline.models.playlist.DataPlayListUser
import com.example.appnghenhaconline.models.playlist.PlayListUser
import com.example.appnghenhaconline.models.playlist.Playlist
import com.example.appnghenhaconline.models.song.DataSong
import com.example.appnghenhaconline.models.song.Song
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchToAddPlaylistFragment: Fragment() {
    internal lateinit var view: View
    private lateinit var edtSearch: EditText
    private lateinit var btnBack: ImageView
    private lateinit var idPlaylist : String
    private lateinit var listSong : ArrayList<Song>
    private lateinit var listPlaylistSelected : ArrayList<Playlist>
    private lateinit var songAdapter: SongAddAdapter
    private lateinit var playlistSelectedAdapter: PlaylistSelectedAdapter
    private lateinit var rcvSong: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fm_search_to_add_playlist_fragment, container, false)

        init()
        return view
    }

    private fun init(){
        btnBack = view.findViewById(R.id.btnBack)
        edtSearch = view.findViewById(R.id.edtSearch)
        initObjectPlaylist()
        initListSong()
        event()
    }

    private fun event() {
        btnBack.setOnClickListener {
            val bundleReceive = Bundle()
            val fragmentLayout = AddPlaylistFragment()
            bundleReceive.putSerializable("id_playlist", idPlaylist)
            fragmentLayout.arguments = bundleReceive

            MyLib.changeFragment(requireActivity(), fragmentLayout)
        }
        myEnter()
    }
    //===========================================================
    //region INIT ADAPTER
    private fun initObjectPlaylist(){
        val bundle = requireArguments()
        if (bundle != null){
            idPlaylist = bundle.getString("id_playlist")!!
        }
    }

    private fun  initListSong() {
        listSong = ArrayList()
        songAdapter = SongAddAdapter(requireContext(),listSong)

        rcvSong = view.findViewById(R.id.rcvSearchSong)
        rcvSong.setHasFixedSize(true)
        rcvSong.layoutManager = LinearLayoutManager(requireContext(),
            LinearLayoutManager.VERTICAL,false)
        rcvSong.adapter = songAdapter

        songAdapter.setOnItemClickListener(object : SongAddAdapter.IonItemClickListener{
            override fun onItemClick(position: Int) {
//                openDialogAddPlaylist(Gravity.CENTER)
                callApiAddPlaylistUser(idPlaylist,listSong[position].id)
            }
        })
    }

//    private fun  initListPlaylist(rcvPlaylistSelected: RecyclerView) { // Vì thêm vào play list  đã nhấn vào nền ko cần show play list
//        listPlaylistSelected = ArrayList()
//        playlistSelectedAdapter = PlaylistSelectedAdapter(requireContext(),listPlaylistSelected)
//
//        rcvPlaylistSelected.setHasFixedSize(true)
//        rcvPlaylistSelected.layoutManager = LinearLayoutManager(requireContext(),
//            LinearLayoutManager.VERTICAL,false)
//        rcvPlaylistSelected.adapter = playlistSelectedAdapter
//
//        playlistSelectedAdapter.setOnItemClickListener(object : PlaylistSelectedAdapter.IonItemClickListener{
//            override fun onItemClick(position: Int) {
//                MyLib.showToast(requireContext(), listPlaylistSelected[position].playlistname)
//            }
//        })
//        callApiPlayList(listPlaylistSelected, playlistSelectedAdapter)
//    }

    //endregion
    //===========================================================
    //region ANOTHER FUNCTION

    // mở dialog thêm vào playlist
//    private fun openDialogAddPlaylist(gravity: Int){
//        val dialog = Dialog(requireContext())
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        dialog.setContentView(R.layout.dlg_select_playlist_dialog)
//        dialog.setCancelable(true)
//
//        val window = dialog.window
//        window?.setLayout(
//            WindowManager.LayoutParams.MATCH_PARENT,
//            WindowManager.LayoutParams.WRAP_CONTENT
//        )
//        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//
//        val windowAttributes: WindowManager.LayoutParams = window!!.attributes
//        windowAttributes.gravity = gravity
//        window.attributes = windowAttributes
//
//        val btnExit: Button = dialog.findViewById(R.id.btnExit)
//        val rcvPlaylistSelected: RecyclerView = dialog.findViewById(R.id.rcvPlaylistSelect)
//
//        initListPlaylist(rcvPlaylistSelected)
//
//        btnExit.setOnClickListener {
//            dialog.dismiss()
//        }
//
//        dialog.show()
//    }

    // sự kiện bấm nút tìm
    private fun myEnter () {
        edtSearch.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    callApiSearchSong(edtSearch.text.toString())
                    MyLib.showLog(edtSearch.text.toString())
                    return true
                }
                return false
            }
        })
    }
    //endregion
    //===========================================================
    //region CALL API
    private fun callApiSearchSong(q : String) {
        ApiService.apiService.searchSongAndSinger(q).enqueue(object : Callback<DataSong?> {
            override fun onResponse(call: Call<DataSong?>, response: Response<DataSong?>) {
                val dataSong = response.body()
                MyLib.showLog(dataSong.toString())
                if(dataSong!=null){
                    if(!dataSong.error){
                        val dtListSong = dataSong.listSong
                        listSong.addAll(dtListSong)
                        songAdapter.notifyDataSetChanged()
                    }else MyLib.showLog(dataSong.message)
                }
            }

            override fun onFailure(call: Call<DataSong?>, t: Throwable) {
                MyLib.showLog(t.toString())
            }
        })
    }

//    private fun callApiPlayList(list : ArrayList<Playlist>, adapter : PlaylistSelectedAdapter) {
//        ApiService.apiService.getPlayList().enqueue(object : Callback<DataPlayList?> {
//            override fun onResponse(call: Call<DataPlayList?>, response: Response<DataPlayList?>) {
//                val dataPlayList = response.body()
//                if(dataPlayList != null) {
//                    if(!dataPlayList.error) {
//                        list.addAll(dataPlayList.listPlayList)
//                        adapter.notifyDataSetChanged()
//                    }
//                    else {
//                        MyLib.showLog("PlayNowFragment.kt: " + dataPlayList.message)
//                    }
//                }
//            }
//
//            override fun onFailure(call: Call<DataPlayList?>, t: Throwable) {
//                MyLib.showToast(requireContext(),"Call Api Error")
//            }
//        })
//    }

    private fun callApiAddPlaylistUser(idPlayList : String, idSong : String) {
        ApiService.apiService.addPlaylistUser(idPlayList,idSong).enqueue(object : Callback<DataPlayListUser?> {
            override fun onResponse( call: Call<DataPlayListUser?>, response: Response<DataPlayListUser?>) {
                val dataPlayListUser = response.body()
                if (dataPlayListUser != null) {
                    if (!dataPlayListUser.error) {
                        MyLib.showToast(requireContext(),dataPlayListUser.message)
                    }
                    else {
                        MyLib.showToast(requireContext(),dataPlayListUser.message)
                    }
                }
            }

            override fun onFailure(call: Call<DataPlayListUser?>, t: Throwable) {
                MyLib.showToast(requireContext(),"Call Api Error")
            }
        })
    }

    //endregion
}