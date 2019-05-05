package com.enesgemci.mymovies.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class ReviewResponse(
    val id: Int,
    val results: List<Review>
) : Parcelable
