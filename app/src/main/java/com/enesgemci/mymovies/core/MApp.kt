package com.enesgemci.mymovies.core

import com.enesgemci.mymovies.BuildConfig
import com.enesgemci.mymovies.di.DaggerAppComponent
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import timber.log.Timber

class MApp : DaggerApplication() {

    private val appComponent = DaggerAppComponent.builder()
        .application(this)
        .build()

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return appComponent
    }
}
