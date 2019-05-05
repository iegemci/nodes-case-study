package com.enesgemci.mymovies.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.enesgemci.mymovies.util.KeywordListConverter
import com.enesgemci.mymovies.util.ReviewListConverter
import com.enesgemci.mymovies.util.VideoListConverter

@Database(entities = [Movie::class, Search::class], version = 1, exportSchema = false)
@TypeConverters(value = [(KeywordListConverter::class), (VideoListConverter::class), (ReviewListConverter::class)])
abstract class AppDatabase : RoomDatabase() {

    abstract fun getMovieDao(): MovieDao

    abstract fun getSearchDao(): SearchDao
}