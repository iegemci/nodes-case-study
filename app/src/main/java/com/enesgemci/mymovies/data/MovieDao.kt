package com.enesgemci.mymovies.data

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface MovieDao {

    @Query("SELECT * FROM movies")
    fun getMoviesLive(): LiveData<List<Movie>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveMovie(movie: Movie)

    @Delete
    fun deleteMovie(movie: Movie)
}