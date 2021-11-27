package com.example.appnghenhaconline.models.album

import com.example.appnghenhaconline.models.playlist.Category

class DataAlbum (
    var error: Boolean,
    var message: String,
    var listAlbum : ArrayList<Album>
) {
    override fun toString(): String {
        return "DataAlbum(error=$error, message='$message', listAlbum=$listAlbum)"
    }
}