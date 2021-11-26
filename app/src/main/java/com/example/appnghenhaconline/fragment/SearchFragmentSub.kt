package com.example.appnghenhaconline.fragment

import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.example.appnghenhaconline.R

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.appnghenhaconline.MyLib
import com.example.appnghenhaconline.adapter.SingerAdapter
import com.example.appnghenhaconline.adapter.SongAdapter
import com.example.appnghenhaconline.api.ApiService
import com.example.appnghenhaconline.dataLocalManager.Service.MyService
import com.example.appnghenhaconline.models.singer.Singer
import com.example.appnghenhaconline.models.song.DataSong
import com.example.appnghenhaconline.models.song.Song
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchFragmentSub: Fragment() {

    internal lateinit var view: View
    lateinit var edtSearch: EditText
    lateinit var btnBack: ImageView
    lateinit var listSong : ArrayList<Song>
    lateinit var listSinger : ArrayList<Singer>
    lateinit var songAdapter: SongAdapter
    lateinit var singerAdapter: SingerAdapter
    lateinit var rcvSong: RecyclerView
    lateinit var rcvSinger: RecyclerView
    lateinit var mediaPlayer : MediaPlayer

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                                savedInstanceState: Bundle?): View? {
        view = inflater.inflate(R.layout.fm_search_fragment_sub, container, false)
        init()
        event()
        return view
    }

    private fun init(){
        edtSearch = view.findViewById(R.id.edtSearch)
        btnBack = view.findViewById(R.id.btnBack)
        initListSong()
        initListSinger()
    }

    private fun  initListSong() {
        mediaPlayer = MediaPlayer()
        listSong = ArrayList()
        songAdapter = SongAdapter(view.context,listSong)

        rcvSong = view.findViewById(R.id.rcvSearchSong)
        rcvSong.setHasFixedSize(true)
        rcvSong.layoutManager = LinearLayoutManager(view.context,
            LinearLayoutManager.VERTICAL,false)
        rcvSong.adapter = songAdapter

        songAdapter.setOnItemClickListener(object : SongAdapter.IonItemClickListener{
            override fun onItemClick(position: Int) {
                if (mediaPlayer.isPlaying){
                    mediaPlayer.stop()
                    clickStartService(listSong, position)
                }else{
                    clickStartService(listSong, position)
                }
                MyLib.showLog("AlbumFragment: "+ listSong[position].link)
            }

            override fun onItemSelected(position: Int) {
//                TODO("Not yet implemented")
            }
        })
    }

    private fun  initListSinger() {
        listSinger = ArrayList()
        singerAdapter = SingerAdapter(view.context,listSinger)

        rcvSinger = view.findViewById(R.id.rcvSearchSinger)
        rcvSinger.setHasFixedSize(true)
        rcvSinger.layoutManager = LinearLayoutManager(view.context,
            LinearLayoutManager.VERTICAL,false)
        rcvSinger.adapter = singerAdapter

        singerAdapter.setOnItemClickListener(object : SingerAdapter.IonItemClickListener{
            override fun onItemClick(position: Int) {
                MyLib.showToast(requireContext(),listSinger[position].singername)
            }
        })
    }

    private fun event(){
        //auto focus vào edittext
        edtSearch.requestFocus()
        edtSearch.showSoftKeyboard()
        //set back cho button
        btnBack.setOnClickListener {
            val fragmentTransaction = fragmentManager?.beginTransaction()
            fragmentTransaction?.replace(R.id.fragmentContainer, SearchFragment())
            fragmentTransaction?.addToBackStack(null)
            fragmentTransaction?.commit()
        }

        myEnter()

    }

    private fun EditText.showSoftKeyboard(){
        (this.context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager)
            .toggleSoftInput(InputMethodManager.SHOW_FORCED,0)
    }

    private fun callApiSearchSong(q : String) {
        ApiService.apiService.searchSongAndSinger(q).enqueue(object : Callback<DataSong?> {
            override fun onResponse(call: Call<DataSong?>, response: Response<DataSong?>) {
                val dataSong = response.body()
                MyLib.showLog(dataSong.toString())
                if(dataSong!=null){
                    if(!dataSong.error){
                        val dtListSinger = dataSong.singer
                        listSinger.addAll(dtListSinger)
                        singerAdapter.notifyDataSetChanged()

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

    private fun clickStartService(list : ArrayList<Song>, position: Int) {
        val intent = Intent(requireContext(), MyService::class.java)

        val bundle = Bundle()
        bundle.putSerializable("position_song", position)
        bundle.putSerializable("list_song", list)
        intent.putExtras(bundle)

        activity?.startService(intent)
    }

}