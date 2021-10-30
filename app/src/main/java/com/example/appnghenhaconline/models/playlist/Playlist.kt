package com.example.appnghenhaconline.models.playlist

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Playlist(
//    var title: String,
//    var resourceId: Int
    @SerializedName("_id")
    var id :String,
    var playlistname : String,
    var image : String,
    var category: Category

) : Serializable{

    override fun toString(): String {
        return "Playlist(id='$id', playlistname='$playlistname', image='$image', category=$category)"
    }
}