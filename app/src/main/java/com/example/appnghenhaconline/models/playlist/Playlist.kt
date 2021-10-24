package com.example.appnghenhaconline.models.playlist

import java.io.Serializable

class Playlist(title: String, resourceId: Int) : Serializable {
    private var title: String = ""
    private var resourceId: Int = 0
    init {
        this.title = title
        this.resourceId = resourceId
    }

    fun setTittle(title: String){
        this.title = title
    }

    fun getTittle() :String{
        return title
    }

    fun setResourceId(resource: Int){
        this.resourceId = resource
    }

    fun getResourceId() :Int{
        return resourceId
    }
}