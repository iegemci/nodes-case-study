package com.enesgemci.mymovies.util

import androidx.room.TypeConverter
import com.enesgemci.mymovies.data.model.Review
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

open class ReviewListConverter {

    @TypeConverter
    fun fromString(value: String): List<Review>? {
        val listType = object : TypeToken<List<Review>>() {}.type
        return Gson().fromJson<List<Review>>(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Review>?): String {
        val gson = Gson()
        return gson.toJson(list)
    }
}
