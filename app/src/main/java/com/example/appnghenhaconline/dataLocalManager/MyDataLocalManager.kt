package com.example.appnghenhaconline.dataLocalManager

import android.content.Context
import com.example.appnghenhaconline.dataLocalManager.SharedPreferences.MySharePreferences
import com.example.appnghenhaconline.models.playlist.Playlist
import com.example.appnghenhaconline.models.song.Song
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

class MyDataLocalManager {

    private lateinit var mySharePreferences: MySharePreferences

    companion object{
        private const val KEY_PREF_FIRST_INSTALL = "PREF_FIRST_INSTALL"
        private const val KEY_PREF_OBJECT_SONG = "PREF_OBJECT_SONG"
        private const val KEY_PREF_PLAYLIST = "PREF_PLAYLIST"
        private const val KEY_PREF_IS_PLAYING = "PREF_IS_PLAYING"
        private const val KEY_PREF_IS_REPEAT = "PREF_IS_REPEAT"
        private const val KEY_PREF_IS_SHUFFLE = "PREF_IS_SHUFFLE"
        private const val KEY_PREF_IS_LYRIC = "PREF_IS_LYRIC"


        private lateinit var instance: MyDataLocalManager

        fun init(context: Context) {
            instance = MyDataLocalManager()
            instance.mySharePreferences = MySharePreferences(context)
        }

        fun getInstance(): MyDataLocalManager{
            if (instance == null) instance = MyDataLocalManager()
            return instance
        }

        //get-set object song
        fun setSong(song: Song){
            val gson = Gson()
            val strJsonSong = gson.toJson(song)
            getInstance().mySharePreferences
                .setStringValue(KEY_PREF_OBJECT_SONG, strJsonSong)
        }

        fun getSong(): Song {
            val gson = Gson()
            val strJsonSong = getInstance().mySharePreferences
                .getStringValue(KEY_PREF_OBJECT_SONG)
            return gson.fromJson(strJsonSong, Song::class.java)
        }

        //get-set object song
        fun setPlaylist(playlist: ArrayList<Song>){
            val gson = Gson()
            val jsonArray = gson.toJsonTree(playlist).asJsonArray
            val strJsonArray = jsonArray.toString()
            getInstance().mySharePreferences
                .setStringValue(KEY_PREF_PLAYLIST, strJsonArray)
        }

        fun getPlaylist(): ArrayList<Song> {
            val gson = Gson()
            val strJsonArray = getInstance().mySharePreferences
                .getStringValue(KEY_PREF_PLAYLIST)
            val playlist: ArrayList<Song> = ArrayList()
            val jsonArray = JSONArray(strJsonArray)
            var jsonObject : JSONObject
            var song : Song
            for ( i in 0 until jsonArray.length()){
                jsonObject = jsonArray.getJSONObject(i)
                song = gson.fromJson(jsonObject.toString(), Song::class.java)
                playlist.add(song)
            }
            return playlist
        }

        //Check đang hát
        fun getIsPlaying(): Boolean {
            return getInstance().mySharePreferences
                .getBooleanValue(KEY_PREF_IS_PLAYING)
        }

        fun setIsPlaying(isPlaying: Boolean){
            getInstance().mySharePreferences
                .setBooleanValue(KEY_PREF_IS_PLAYING, isPlaying)
        }

        //Check đang repeat
        fun getIsRepeat(): Boolean {
            return getInstance().mySharePreferences
                .getBooleanValue(KEY_PREF_IS_REPEAT)
        }

        fun setIsRepeat(isRepeat: Boolean){
            getInstance().mySharePreferences
                .setBooleanValue(KEY_PREF_IS_REPEAT, isRepeat)
        }

        //Check đang shuffle
        fun getIsShuffle(): Boolean {
            return getInstance().mySharePreferences
                .getBooleanValue(KEY_PREF_IS_SHUFFLE)
        }

        fun setIsShuffle(isShuffle: Boolean){
            getInstance().mySharePreferences
                .setBooleanValue(KEY_PREF_IS_SHUFFLE, isShuffle)
        }

        //Check lời bài hát
        fun getIsLyric(): Boolean {
            return getInstance().mySharePreferences
                .getBooleanValue(KEY_PREF_IS_LYRIC)
        }

        fun setIsLyric(isLyric: Boolean){
            getInstance().mySharePreferences
                .setBooleanValue(KEY_PREF_IS_LYRIC, isLyric)
        }

        //Check lần tải đầu tiên
        fun getFirstInstalled(): Boolean {
            return getInstance().mySharePreferences
                .getBooleanValue(KEY_PREF_FIRST_INSTALL)
        }

        fun setFirstInstalled(isFirst: Boolean){
            getInstance().mySharePreferences
                .setBooleanValue(KEY_PREF_FIRST_INSTALL, isFirst)
        }
    }
}