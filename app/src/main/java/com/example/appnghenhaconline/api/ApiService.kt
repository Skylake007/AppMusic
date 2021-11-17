package com.example.appnghenhaconline.api

import com.example.appnghenhaconline.models.playlist.DataPlayList
import com.example.appnghenhaconline.models.song.DataSong
import com.example.appnghenhaconline.models.user.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    @GET("listsong") //API GET ListSong
    fun getListSong() : Call<DataSong>

    @GET("/listsong/playlist") // API GET ListSong With ID
    fun getListSongByID(@Query ("id") id : String) : Call<DataSong>

    companion object{
        private val baseUrl = "http://192.168.1.4:3000/"
        //private val baseUrl = "http://192.168.10.62:3000/"
//      private val baseUrl = "http://192.168.0.31:3000/"
        private val gson: Gson = GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create()
        val apiService: ApiService = Retrofit.Builder()
            .baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(ApiService::class.java)
    }

    @GET("LogIn") //API GET Login
    fun getLogIn(@Query("username") username : String,
                  @Query("password") password : String
    ) : Call<DataUser>

    @POST("SignUp") //API POST SignUp
    fun postSignUp(@Query("name") name : String,
                   @Query("password") password: String,
                   @Query("sex") sex : Boolean,
                   @Query("email") email : String
    ) : Call<DataUserSignUp>

    @GET("getPlayList") // API GET getPlayList
    fun getPlayList() : Call<DataPlayList>

    @PUT("UpdateUser") // API PUT UpdateUser
    fun putUpdateUser(@Query("email") email: String,
                     @Query("name") name: String,
                     @Query("sex") sex: Boolean
    ) : Call<UpdateUser>

    @PUT("UpdateUser/UpdatePassword") // API PUT UpdatePasswordUser
    fun putUpdateUserPassword(@Query("email") email: String,
                             @Query("oldPassword") oldPassword :String,
                             @Query("newPassword") newPassword :String

    ) : Call<UpdateUser>

    @PUT("ResetPassword/ReceiveEmail")
    fun sendEmailForgotPassword(@Query("email") email : String) : Call<UserForgotPassword>

    @GET("ResetPassword/ReceiveEmailAndCode")
    fun sendResetCodeForgotPassword(@Query("email") email: String,
                                    @Query("resetCode") resetCode : String

    ) : Call<UserForgotPassword>

    @PUT("ResetPassword/ReceiveEmailCodeAndPassword")
    fun sendNewPasswordForgotPassword(@Query("email") email: String,
                                      @Query("resetCode") resetCode: String,
                                      @Query("password") password: String
    ) : Call<UserForgotPassword>

    @GET("search")
    fun searchSongAndSinger(@Query("q") q : String) : Call<DataSong>

    @PUT("UpdateUser/AddLoveOrRemovePlaylist")
    fun followOrUnfollowPlayList(@Query("userId") userId : String,
                                 @Query("playlistId") playListId : String,
                                 @Query("status") status : Boolean
    ) : Call<DataUser>
}