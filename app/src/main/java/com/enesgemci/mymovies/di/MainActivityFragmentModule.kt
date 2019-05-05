package com.enesgemci.mymovies.di

import com.enesgemci.mymovies.ui.movies.MoviesFragment
import com.enesgemci.mymovies.ui.mymovies.MyMoviesFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class MainActivityFragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeMoviesFragment(): MoviesFragment

    @ContributesAndroidInjector
    abstract fun contributeMyMoviesFragment(): MyMoviesFragment
}