package com.example.appnghenhaconline.models.user

import com.example.appnghenhaconline.models.Playlist
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class User  (
    @SerializedName("_id")
    var id : String,
    var username : String,
    var password : String,
    var name : String,
    var sex : Boolean,
    var email : String,
    var followPlaylist : ArrayList<Playlist>,
) : Serializable {
    override fun toString(): String {
        return "User(id='$id', username='$username', password='$password', name='$name', sex=$sex, email='$email', followPlaylist=$followPlaylist)"
    }
}
