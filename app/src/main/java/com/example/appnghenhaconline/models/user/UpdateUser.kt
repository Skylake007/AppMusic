package com.example.appnghenhaconline.models.user

class UpdateUser (
    var error: Boolean,
    var message: String,
    var user: User
) {
    override fun toString(): String {
        return "UpdateUser(error=$error, message='$message', user=$user)"
    }
}