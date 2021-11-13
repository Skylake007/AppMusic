package com.example.appnghenhaconline.models.song

import com.example.appnghenhaconline.models.singer.Singer

class DataSong(
    var error: Boolean,
    var message: String,
    var singer : ArrayList<Singer>,
    var listSong: ArrayList<Song>
) {
    override fun toString(): String {
        return "DataSong(error=$error, message='$message', singer=$singer, listSong=$listSong)"
    }
}