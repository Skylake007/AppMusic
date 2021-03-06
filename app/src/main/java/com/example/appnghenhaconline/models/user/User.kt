package com.example.appnghenhaconline.models.user

import com.example.appnghenhaconline.models.album.Album
import com.example.appnghenhaconline.models.playlist.Playlist
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
    var avatar : String,
    var followPlaylist : ArrayList<Playlist>,
    var followAlbum : ArrayList<Album>,
    var favoriteSinger : ArrayList<String>
) : Serializable {
    override fun toString(): String {
        return "User(id='$id', username='$username', password='$password', name='$name', sex=$sex, email='$email', avatar='$avatar', followPlaylist=$followPlaylist, followAlbum=$followAlbum, favoriteSinger=$favoriteSinger)"
    }
}
