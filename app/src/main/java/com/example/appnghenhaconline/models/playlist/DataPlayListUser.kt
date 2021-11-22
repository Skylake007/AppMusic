package com.example.appnghenhaconline.models.playlist

class DataPlayListUser (
    var error: Boolean,
    var message: String,
    var listPlayListUser : ArrayList<PlayListUser>,
    var playlistUser : PlayListUser

) {
    override fun toString(): String {
        return "DataPlayListUser(error=$error, message='$message', listPlayListUser=$listPlayListUser, playListUser=$playlistUser)"
    }
}