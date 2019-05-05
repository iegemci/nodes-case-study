package com.enesgemci.mymovies.di

import com.enesgemci.mymovies.ui.MainActivity
import com.enesgemci.mymovies.ui.details.MovieDetailsActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector(modules = [MainActivityFragmentModule::class])
    internal abstract fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [MovieDetailActivityFragmentModule::class])
    internal abstract fun contributeMovieDetailsActivity(): MovieDetailsActivity
}
