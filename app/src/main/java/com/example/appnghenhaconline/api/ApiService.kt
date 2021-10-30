package com.example.appnghenhaconline.api

import com.example.appnghenhaconline.models.playlist.DataPlayList
import com.example.appnghenhaconline.models.user.DataUser
import com.example.appnghenhaconline.models.song.DataSong
import com.example.appnghenhaconline.models.user.DataUserSignUp
import com.example.appnghenhaconline.models.user.User
import com.example.appnghenhaconline.models.user.UserSignUp
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    @GET("listsong")
    fun getListSong() : Call<DataSong>

    companion object{
        //private val baseUrl = "http://192.168.1.11:3000/"
        //private val baseUrl = "http://192.168.10.62:3000/"
        private val baseUrl = "http://192.168.0.31:3000/"
        private val gson: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
        val apiService: ApiService = Retrofit.Builder()
            .baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }

    @GET("LogIn")
    fun getLogIn(@Query("username") username : String,
                  @Query("password") password : String
    ) : Call<DataUser>

    @POST("SignUp")
    fun postSignUp(@Query("name") name : String,
                   @Query("password") password: String,
                   @Query("sex") sex : Boolean,
                   @Query("email") email : String
    ) : Call<DataUserSignUp>

    @GET("LoadPlayList")
    fun getPlayList() : Call<DataPlayList>
}