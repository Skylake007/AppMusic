package com.example.appnghenhaconline.fragment.Library.Playlist

import android.content.Context
import android.os.Bundle
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.R
import com.example.appnghenhaconline.adapter.SongAddAdapter
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.models.playlist.DataPlayListUser
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
    private lateinit var songAdapter: SongAddAdapter
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
        event()
    }

    private fun event() {
        edtSearch.showSoftKeyboard()
        btnBack.setOnClickListener {
            val bundleReceive = Bundle()
            val fragmentLayout = AddPlaylistFragment()
            bundleReceive.putSerializable("id_playlist", idPlaylist)
            fragmentLayout.arguments = bundleReceive

            MyLib.changeFragment(requireActivity(), fragmentLayout)
            edtSearch.closeSoftKeyboard()
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

    private fun EditText.closeSoftKeyboard(){
        (this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .toggleSoftInput(InputMethodManager.HIDE_NOT_ALWAYS,0)
    }

    private fun EditText.showSoftKeyboard(){
        (this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
            .toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
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

    //endregion
    //===========================================================
    //region ANOTHER FUNCTION

    // sự kiện bấm nút tìm
    private fun myEnter () {
        edtSearch.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                if (event!!.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    initListSong()
                    callApiSearchSong(edtSearch.text.toString())
                    MyLib.showLog(edtSearch.text.toString())
                    edtSearch.closeSoftKeyboard()
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