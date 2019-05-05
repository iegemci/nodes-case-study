package com.enesgemci.mymovies.viewmodel

import androidx.lifecycle.MutableLiveData
import com.enesgemci.mymovies.core.base.BaseViewModel
import com.enesgemci.mymovies.data.Movie
import com.enesgemci.mymovies.data.Search
import com.enesgemci.mymovies.repository.MoviesRepository
import com.enesgemci.mymovies.repository.SearchRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val moviesRepository: MoviesRepository
) : BaseViewModel() {

    val searchPredictionsLiveData = MutableLiveData<List<Search>>()
    val lastSearchQueryLiveData = MutableLiveData<String>()

    val savedMoviesLiveData = moviesRepository.getMyMoviesLive()

    val moviesLiveData = MutableLiveData<List<Movie>>()
    val lastPageLiveData = MutableLiveData<Boolean>()
    val lastSearchLiveData = MutableLiveData<String>()

    private val innerMoviesListLiveData = MutableLiveData<ArrayList<Movie>>()

    fun getSearchPredictions(query: String) = launch {
        val data = searchRepository.getSimilarSearches(query)
        searchPredictionsLiveData.value = data
    }

    fun saveLastSearchQuery(query: String) = launch {
        lastSearchQueryLiveData.value = query
        searchRepository.saveSearch(query)
    }

    fun deleteSearchQuery(query: String) = launch {
        searchRepository.deleteSearch(query)
    }

    fun getLastSearches() = launch {
        searchPredictionsLiveData.value = searchRepository.getLastSearches()
    }

    fun searchMovies(query: String, page: Int = 1) {
        lastSearchLiveData.value = query

        if (page == 1) {
            loading.value = true
            innerMoviesListLiveData.value = arrayListOf()
            lastPageLiveData.value = false
            moviesLiveData.value = listOf()
        }

        launch {
            try {
                val result = moviesRepository.getMovies(query, page)
                val movies = result.results

                innerMoviesListLiveData.value?.addAll(movies)
                lastPageLiveData.value = result.page == result.totalPages
                moviesLiveData.value = innerMoviesListLiveData.value.orEmpty()
            } catch (e: Exception) {
                error.value = e
            } finally {
                loading.value = false
            }
        }
    }

    fun saveMovie(movie: Movie) = launch {
        moviesRepository.saveMovie(movie)
    }

    fun deleteMovie(movie: Movie) = launch {
        moviesRepository.deleteMovie(movie)
    }

    fun mergeMovies(movies: List<Movie>, onSuccess: (list: List<Movie>) -> Unit) {
        if (movies.isNotEmpty()) {
            launch {
                val savedMovies = savedMoviesLiveData.value.orEmpty()

                movies.forEach { movie ->
                    movie.saved =
                        savedMovies.firstOrNull { it.title == movie.title && it.overview == movie.overview } != null
                }

                onSuccess(movies)
            }
        }
    }
}