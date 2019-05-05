package com.enesgemci.mymovies.data.model

import com.enesgemci.mymovies.data.Movie
import com.google.gson.annotations.SerializedName

data class MovieResponse(
    var page: Int,
    var results: List<Movie>,
    @SerializedName("total_results") var totalResults: Int,
    @SerializedName("total_pages") var totalPages: Int
)