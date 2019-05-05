package com.enesgemci.mymovies.repository

import com.enesgemci.mymovies.data.Movie
import com.enesgemci.mymovies.data.MovieDao
import com.enesgemci.mymovies.network.MovieAPI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MoviesRepository @Inject constructor(
    private val movieDao: MovieDao,
    private val movieAPI: MovieAPI
) {

    fun getMyMoviesLive() = movieDao.getMoviesLive()

    suspend fun saveMovie(movie: Movie) = withContext(Dispatchers.IO) {
        return@withContext movieDao.saveMovie(movie.copy(saved = true))
    }

    suspend fun deleteMovie(movie: Movie) = withContext(Dispatchers.IO) {
        return@withContext movieDao.deleteMovie(movie)
    }

    suspend fun getMovies(query: String, page: Int) = withContext(Dispatchers.IO) {
        return@withContext movieAPI.searchMoviesAsync(query, page).await()
    }

    suspend fun getVideos(movieId: Int) = withContext(Dispatchers.IO) {
        return@withContext movieAPI.getVideosAsync(movieId).await()
    }

    suspend fun getKeywords(movieId: Int) = withContext(Dispatchers.IO) {
        return@withContext movieAPI.getKeywordsAsync(movieId).await()
    }

    suspend fun getReviews(movieId: Int) = withContext(Dispatchers.IO) {
        return@withContext movieAPI.getReviewsAsync(movieId).await()
    }
}