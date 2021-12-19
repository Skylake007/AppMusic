package com.example.appnghenhaconline.models.singer

class DataSinger(
    var error: Boolean,
    var message: String,
    var singers : ArrayList<Singer>
){
    override fun toString(): String {
        return "DataSinger(error=$error, message='$message', singers=$singers)"
    }
}