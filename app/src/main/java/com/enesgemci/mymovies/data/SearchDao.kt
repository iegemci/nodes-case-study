package com.enesgemci.mymovies.data

import androidx.room.*

@Dao
interface SearchDao {

    @Query("SELECT * FROM searches ORDER BY lastUsedTime DESC LIMIT 5")
    fun getLastSearches(): List<Search>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveSearch(search: Search)

    @Delete
    fun deleteSearch(search: Search)

    @Query("SELECT * FROM searches WHERE title LIKE :query ORDER BY lastUsedTime DESC LIMIT 5")
    fun getSimilarSearches(query: String): List<Search>
}