package com.example.appnghenhaconline.models.playlist

import com.google.gson.annotations.SerializedName


class DataCategories (
    var error: Boolean,
    var message: String,
    @SerializedName("category")
    var listCategory : ArrayList<Category>
) {
    override fun toString(): String {
        return "DataCategories(error=$error, message='$message', listCategory=$listCategory)"
    }
}