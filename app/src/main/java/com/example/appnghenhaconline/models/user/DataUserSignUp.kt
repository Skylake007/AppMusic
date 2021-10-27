package com.example.appnghenhaconline.models.user

class DataUserSignUp (
    var error: Boolean,
    var message: String,
) {
    override fun toString(): String {
        return "DataUserSignUp(error=$error, message='$message')"
    }
}