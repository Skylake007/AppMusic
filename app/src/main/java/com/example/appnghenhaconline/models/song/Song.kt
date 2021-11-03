package com.example.appnghenhaconline.models.song

import com.example.appnghenhaconline.models.playlist.Category
import com.example.appnghenhaconline.models.playlist.Playlist
import com.example.appnghenhaconline.models.singer.Singer
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Song(
    @SerializedName("_id")
    var id: String,
    var image: String,
    var link: String,
    var title : String,
    var category: ArrayList<Category>,
    var singer: List<Singer>,
    @SerializedName("playlistid")
    var playlistId: ArrayList<String>
) : Serializable {
    override fun toString(): String {
        return "Song(id='$id', image='$image', link='$link', title='$title', category=$category, singer=$singer, playlistId=$playlistId)"
    }
}