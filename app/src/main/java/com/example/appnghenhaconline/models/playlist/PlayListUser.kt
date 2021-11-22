package com.example.appnghenhaconline.models.playlist

import com.example.appnghenhaconline.models.song.Song
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PlayListUser (
    @SerializedName("_id")
    var id : String,
    var idUser : String,
    var playlistName : String,
    @SerializedName("song")
    var listSong : ArrayList<Song>

) : Serializable {
    override fun toString(): String {
        return "PlayListUser(id='$id', idUser='$idUser', playlistName='$playlistName', listSong=$listSong)"
    }
}