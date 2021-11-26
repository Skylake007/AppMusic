package com.example.appnghenhaconline.models.user

class UserSignUp (
    var name : String,
    var password : String,
    var sex : Boolean,
    var email : String,
) {
    override fun toString(): String {
        return "UserSignUp(name='$name', password='$password', sex=$sex, email='$email')"
    }
}