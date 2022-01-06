package com.example.appnghenhaconline.api

import com.example.appnghenhaconline.models.album.DataAlbum
import com.example.appnghenhaconline.models.playlist.DataCategories
import com.example.appnghenhaconline.models.playlist.DataPlayList
import com.example.appnghenhaconline.models.playlist.DataPlayListUser
import com.example.appnghenhaconline.models.playlist.Playlist
import com.example.appnghenhaconline.models.singer.DataSinger
import com.example.appnghenhaconline.models.song.DataSong
import com.example.appnghenhaconline.models.user.*
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    @GET("listsong") //API GET ListSong
    fun getListSong() : Call<DataSong>

    @GET("/listsong/playlist") // API GET ListSong With ID
    fun getListSongByID(@Query ("playlistId") id : String) : Call<DataSong>

    companion object{
        //private val baseUrl = "http://192.168.10.62:3000/"
//        private val baseUrl = "http://192.168.1.9:3000/"
        private val baseUrl = "http://192.168.1.43:3000/"
//      private val baseUrl = "http://192.168.0.31:3000/"
//        private val baseUrl = "http://192.168.0.155:3000/"
//        private val baseUrl = "http://192.168.2.3:3000/"
//        private val baseUrl = "https://teacup-music.herokuapp.com/"
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

    @GET("PlayList") // API GET getPlayList
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

    @PUT("/updateuser/AddLoveOrRemoveAlbum")
    fun followOrUnfollowALbum(@Query("userId") userId : String,
                                 @Query("albumId") albumId : String,
                                 @Query("status") status : Boolean
    ) : Call<DataUser>

    @POST("updateuser/CreatePlaylistUser")
    fun createPlaylistUser(@Query("idUser") idUser : String,
                           @Query("playlistName") playlistName : String

    ) : Call<DataPlayListUser>

    @GET("updateuser/LoadPlaylistUser")
    fun loadPlaylistUser(@Query("idUser") idUser: String) : Call<DataPlayListUser>

    @POST("updateuser/AddNewSongToPlaylistUser")
    fun addPlaylistUser(@Query("idPlaylist") idPlaylist : String,
                        @Query("idSong") idSong : String

    ) : Call<DataPlayListUser>

    @GET ("updateuser/ShowSongFromPlaylistUser")
    fun showSongFromPlaylistUser(@Query("idPlaylist") idPlaylist: String) : Call<DataPlayListUser>

    @PUT("/updateuser/UpdateNameFromPlaylistUser")
    fun updateNameFromPlayListUser(@Query("idPlaylist") idPlaylist: String,
                               @Query("namePlaylist") namePlayList : String
    ) : Call<DataPlayListUser>

    @DELETE("updateuser/RemovePlaylistUser")
    fun deletePlaylistUser(@Query("idPlaylist") idPlaylist: String) : Call<DataPlayListUser>

    @DELETE("updateuser/RemoveSongFromPlaylistUser")
    fun deleteSongFromPlaylistUser(@Query("idPlaylist") idPlaylist: String,
                                   @Query("idSong") idSong: String
    ) : Call<DataPlayListUser>

    @GET("/category/getAllCategories")
    fun getListCategories() : Call<DataCategories>

    @GET("/PlayList/getPlaylistByCategoryId")
    fun getPlaylistByCategoryID(@Query("CategoryId") categoryId : String) : Call<DataPlayList>

    @GET("/album/getAllAlbum")
    fun getAllAlbum() : Call<DataAlbum>

    @GET("/listsong/album")
    fun getListSongByAlbumId(@Query("albumId") albumId: String) : Call<DataSong>

    @GET("/listsong/singer")
    fun getListSongBySingerId(@Query("singerid") SingerId: String) : Call<DataSong>

    @GET("/album/getSingerAlbum")
    fun getListAlbumBySingerId(@Query("idSinger") SingerId: String) : Call<DataAlbum>

    @PUT("updateuser/AddFavoriteSinger")
    fun followSinger(@Query("singerid") SingerId: String,
                     @Query("userid") UserId : String

    )  : Call<DataUser>

    @PUT("/updateuser/removeFavoriteSinger")
    fun unFollowSinger(@Query("singerid") SingerId: String,
                       @Query("userid") UserId : String

    )  : Call<DataUser>

    @Multipart
    @PUT("/updateuser/UpdateAvatarUser")
    fun updateAvatarUser (@Part image : MultipartBody.Part,
                          @Part("idUser") idUser : RequestBody
    ) : Call<DataUser>

    @FormUrlEncoded
    @POST("/singer/getListSinger?_method=GET")
    fun getListLoveSinger(@Field ("singerIds[]") listSingerIds : ArrayList<String>)
    : Call<DataSinger>
}