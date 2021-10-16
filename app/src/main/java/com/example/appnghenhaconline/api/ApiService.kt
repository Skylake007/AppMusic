package com.example.appnghenhaconline.api

import com.example.appnghenhaconline.models.song.DataSong
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface ApiService {

    @GET("listsong")
    fun getListSong() : Call<DataSong>

    companion object{
        private val baseUrl = "http://192.168.0.31:3000/"
        private val gson: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
        val apiService: ApiService = Retrofit.Builder()
            .baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }
}