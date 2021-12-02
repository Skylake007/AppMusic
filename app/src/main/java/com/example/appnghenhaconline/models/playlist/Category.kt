package com.example.appnghenhaconline.models.playlist

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Category(
//    var type: Int,
//    var nameCategory: String,
//    var playlists: ArrayList<Playlist>
    @SerializedName("_id")
    var id : String,
    var categoryname : String,
    var imageCategory : String

) : Serializable {

    override fun toString(): String {
        return "Category(id='$id', categoryname='$categoryname', imageCategory='$imageCategory')"
    }
}
