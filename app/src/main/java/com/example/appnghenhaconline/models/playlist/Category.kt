package com.example.appnghenhaconline.models.playlist

class Category(type: Int, nameCategory: String, playlists: ArrayList<Playlist>) {
    private var type: Int = 0
    private var nameCategory: String = ""
    private var playlists: ArrayList<Playlist>

    init {
        this.type = type
        this.nameCategory = nameCategory
        this.playlists = playlists
    }

    fun setType(type: Int){
        this.type = type
    }

    fun getType(): Int{
        return type
    }

    fun setNameCategory(nameCategory: String){
        this.nameCategory = nameCategory
    }

    fun getNameCategory() :String{
        return nameCategory
    }

    fun setPlaylists(playlists: ArrayList<Playlist>){
        this.playlists = playlists
    }

    fun getPlaylists() : ArrayList<Playlist>{
        return playlists
    }
}