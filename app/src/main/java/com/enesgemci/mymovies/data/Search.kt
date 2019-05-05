package com.enesgemci.mymovies.data

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "searches")
data class Search(
    @PrimaryKey val title: String = "",
    val lastUsedTime: Long = System.currentTimeMillis()
) : Parcelable