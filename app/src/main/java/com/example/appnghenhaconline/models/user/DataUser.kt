package com.example.appnghenhaconline.models.user

class DataUser(
    var error: Boolean,
    var message: String,
    var user: User
) {
    override fun toString(): String {
        return "DataUser(error=$error, message='$message', user=$user)"
    }
}