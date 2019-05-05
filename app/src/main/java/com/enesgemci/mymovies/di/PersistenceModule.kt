package com.enesgemci.mymovies.di

import android.app.Application
import androidx.annotation.NonNull
import androidx.room.Room
import com.enesgemci.mymovies.data.AppDatabase
import com.enesgemci.mymovies.data.MovieDao
import com.enesgemci.mymovies.data.SearchDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class PersistenceModule {

    @Provides
    @Singleton
    fun provideDatabase(@NonNull application: Application): AppDatabase {
        return Room.databaseBuilder(
            application,
            AppDatabase::class.java,
            "Movies.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideMoviesDao(@NonNull database: AppDatabase): MovieDao {
        return database.getMovieDao()
    }

    @Provides
    @Singleton
    fun provideSearchDao(@NonNull database: AppDatabase): SearchDao {
        return database.getSearchDao()
    }
}