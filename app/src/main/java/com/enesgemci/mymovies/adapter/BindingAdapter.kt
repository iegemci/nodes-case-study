package com.enesgemci.mymovies.adapter

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.enesgemci.mymovies.network.ApiConstants

@BindingAdapter("poster")
fun bindPoster(view: ImageView, image: String) {
    if (image.isNotEmpty()) {
        Glide.with(view.context)
            .load(ApiConstants.getBackdropPath(image))
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(view)
    }
}
