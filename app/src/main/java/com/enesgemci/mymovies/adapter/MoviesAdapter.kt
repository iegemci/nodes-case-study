package com.enesgemci.mymovies.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.enesgemci.mymovies.R
import com.enesgemci.mymovies.data.Movie
import com.enesgemci.mymovies.databinding.ListItemMovieBinding
import com.enesgemci.mymovies.network.ApiConstants
import com.enesgemci.mymovies.view.like.AnimatedActionButton
import com.enesgemci.mymovies.viewmodel.MovieItemViewModel
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette

class MoviesAdapter(
    private val itemClickFunc: (movie: Movie) -> Unit,
    private val likeFunc: (movie: Movie, liked: Boolean) -> Unit
) : ListAdapter<Movie, MoviesAdapter.ViewHolder>(MoviesDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_movie, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position).let { model ->
            with(holder) {
                itemView.tag = model
                bind(model)
            }
        }
    }

    inner class ViewHolder(
        private val binding: ListItemMovieBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(movie: Movie) {
            movie.posterPath?.let {
                Glide.with(binding.root.context)
                    .load(ApiConstants.getPosterPath(it))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .listener(
                        GlidePalette.with(ApiConstants.getPosterPath(it))
                            .use(BitmapPalette.Profile.VIBRANT)
                            .intoBackground(binding.itemTitleBg)
                            .crossfade(true)
                    )
                    .into(binding.itemPoster)
            }

            binding.likeButton.likeListener = object : AnimatedActionButton.OnActionListener {
                override fun liked(animatedActionButton: AnimatedActionButton) {
                    movie.saved = true
                    likeFunc(movie, true)
                }

                override fun unLiked(animatedActionButton: AnimatedActionButton) {
                    movie.saved = false
                    likeFunc(movie, false)
                }
            }

            with(binding) {
                onClickListener = View.OnClickListener {
                    itemClickFunc(movie)
                }
                vm = MovieItemViewModel(movie)
                executePendingBindings()
            }
        }
    }
}

private class MoviesDiffCallback : DiffUtil.ItemCallback<Movie>() {

    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.title == newItem.title
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.saved == newItem.saved
    }
}
