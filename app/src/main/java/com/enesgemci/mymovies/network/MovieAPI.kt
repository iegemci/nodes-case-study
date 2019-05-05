package com.enesgemci.mymovies.network

import com.enesgemci.mymovies.data.model.KeywordResponse
import com.enesgemci.mymovies.data.model.MovieResponse
import com.enesgemci.mymovies.data.model.ReviewResponse
import com.enesgemci.mymovies.data.model.VideoResponse
import kotlinx.coroutines.Deferred
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MovieAPI {

    @GET("search/movie")
    fun searchMoviesAsync(@Query("query") query: String, @Query("page") page: Int): Deferred<MovieResponse>

    @GET("movie/{movie_id}/keywords")
    fun getKeywordsAsync(@Path("movie_id") id: Int): Deferred<KeywordResponse>

    @GET("movie/{movie_id}/videos")
    fun getVideosAsync(@Path("movie_id") id: Int): Deferred<VideoResponse>

    @GET("movie/{movie_id}/reviews")
    fun getReviewsAsync(@Path("movie_id") id: Int): Deferred<ReviewResponse>
}