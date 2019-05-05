package com.enesgemci.mymovies.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.enesgemci.mymovies.mvvm.AppViewModelFactory
import com.enesgemci.mymovies.viewmodel.MainViewModel
import com.enesgemci.mymovies.viewmodel.MovieDetailViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
internal abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(MainViewModel::class)
    internal abstract fun bindMainViewModel(mainViewModel: MainViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailViewModel::class)
    internal abstract fun bindMovieDetailViewModel(movieDetailViewModel: MovieDetailViewModel): ViewModel

    @Binds
    internal abstract fun bindViewModelFactory(factory: AppViewModelFactory): ViewModelProvider.Factory
}
