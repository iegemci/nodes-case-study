package com.enesgemci.mymovies.viewmodel

import androidx.databinding.ObservableField
import androidx.lifecycle.ViewModel
import com.enesgemci.mymovies.data.Movie

class MovieItemViewModel(movie: Movie) : ViewModel() {

    val title = ObservableField<String>(movie.title)
    val saved = ObservableField<Boolean>(movie.saved)
}