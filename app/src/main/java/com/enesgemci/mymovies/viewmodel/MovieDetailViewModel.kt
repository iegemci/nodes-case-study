package com.enesgemci.mymovies.viewmodel

import androidx.databinding.ObservableField
import com.enesgemci.mymovies.core.base.BaseViewModel
import com.enesgemci.mymovies.data.Movie
import javax.inject.Inject

class MovieDetailViewModel @Inject constructor() : BaseViewModel() {

    val poster = ObservableField<String>()
    val releaseDate = ObservableField<String>()
    val title = ObservableField<String>()
    val rating = ObservableField<Float>()
    val overview = ObservableField<String>()

    fun setFields(movie: Movie) {
        poster.set(movie.backdropPath ?: movie.posterPath)
        releaseDate.set(movie.releaseDate)
        title.set(movie.title)
        rating.set(movie.voteAverage / 2)
        overview.set(movie.overview)
    }
}