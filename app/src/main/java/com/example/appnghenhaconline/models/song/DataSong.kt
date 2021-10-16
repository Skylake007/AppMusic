package com.example.appnghenhaconline.models.song

class DataSong(
    var error: Boolean,
    var message: String,
    var listSong: ArrayList<Song>
) {
    override fun toString(): String {
        return "DataSong: error: $error, message: $message, listsong: $listSong \n"
    }
}