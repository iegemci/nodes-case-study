package com.enesgemci.mymovies.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class KeywordResponse(
    val id: Int,
    val keywords: List<Keyword>
) : Parcelable
