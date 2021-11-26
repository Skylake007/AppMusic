package com.example.appnghenhaconline.models.singer

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Singer(
    @SerializedName("_id")
    var id : String,
    var singername : String,
    var image : String,
) : Serializable {
    override fun toString(): String {
        return "Singer(id='$id', singername='$singername', image='$image')"
    }
}