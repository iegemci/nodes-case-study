package com.enesgemci.mymovies.repository

import com.enesgemci.mymovies.data.Search
import com.enesgemci.mymovies.data.SearchDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class SearchRepository @Inject constructor(private val searchDao: SearchDao) {

    suspend fun getSimilarSearches(query: String) = withContext(Dispatchers.IO) {
        val searchQuery = "$query%"
        return@withContext searchDao.getSimilarSearches(searchQuery)
    }

    suspend fun saveSearch(query: String) = withContext(Dispatchers.IO) {
        return@withContext searchDao.saveSearch(Search(query.trim()))
    }

    suspend fun deleteSearch(query: String) = withContext(Dispatchers.IO) {
        return@withContext searchDao.deleteSearch(Search(query.trim()))
    }

    suspend fun getLastSearches() = withContext(Dispatchers.IO) {
        return@withContext searchDao.getLastSearches()
    }
}