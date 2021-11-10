package com.example.appnghenhaconline.models.user

class UserForgotPassword (
    var error: Boolean,
    var message: String,
    var resetCode: String,
    var email: String
) {
    override fun toString(): String {
        return "UserForgotPassword(error=$error, message='$message', resetCode='$resetCode', email='$email')"
    }
}