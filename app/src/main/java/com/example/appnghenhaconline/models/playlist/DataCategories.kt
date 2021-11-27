package com.example.appnghenhaconline.models.playlist


class DataCategories (
    var error: Boolean,
    var message: String,
    var listCategory : ArrayList<Category>
) {
    override fun toString(): String {
        return "DataCategories(error=$error, message='$message', listCategory=$listCategory)"
    }
}