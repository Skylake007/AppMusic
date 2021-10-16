package com.example.appnghenhaconline.models.song

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Song(
    @SerializedName("_id")
    var id: String,
    var title: String,
    var image: String,
    var link: String,
) : Serializable {
    override fun toString(): String {
        return "Song: id: $id, title: $title, image: $image, link:  $link \n"
    }
}