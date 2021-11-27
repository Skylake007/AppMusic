package com.example.appnghenhaconline.models.album

import com.example.appnghenhaconline.models.singer.Singer
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Album (
    @SerializedName("_id")
    var id : String,
    var albumname : String,
    var singer : Singer,
    var imageAlbum: String

) : Serializable {

    override fun toString(): String {
        return "Album(id='$id', albumname='$albumname', singer=$singer, imageAlbum='$imageAlbum')"
    }
}