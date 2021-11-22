package com.example.appnghenhaconline.models.playlist

import com.example.appnghenhaconline.models.song.Song
import java.io.Serializable

class DataPlayList (
    var error: Boolean,
    var message: String,
    var listPlayList: ArrayList<Playlist>,
    var playlistUser : Playlist
) {
    override fun toString(): String {
        return "DataPlayList(error=$error, message='$message', listPlayList=$listPlayList, playListUser=$playlistUser)"
    }
}