package com.example.appnghenhaconline.models.playlist

import android.os.Parcelable
import java.io.Serializable

class Playlist(var id: String,
               var playlistName: String,
               var image: Int,
               var category: Category) : Serializable {

}