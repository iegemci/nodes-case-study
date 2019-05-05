package com.enesgemci.mymovies.ui.details

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.navArgs
import com.enesgemci.mymovies.R
import com.enesgemci.mymovies.core.base.BaseActivity
import com.enesgemci.mymovies.databinding.ActivityMovieDetailBinding
import com.enesgemci.mymovies.viewmodel.MovieDetailViewModel
import com.google.android.material.appbar.CollapsingToolbarLayout
import javax.inject.Inject

class MovieDetailsActivity : BaseActivity() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val args: MovieDetailsActivityArgs by navArgs()

    private val viewModel: MovieDetailViewModel
            by lazy { ViewModelProviders.of(this, viewModelFactory).get(MovieDetailViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMovieDetailBinding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_movie_detail
        )

        viewModel.setFields(args.movie)

        binding.vm = viewModel

        setupToolbar(binding.toolbar)
    }

    private fun setupToolbar(toolbar: Toolbar) {
        toolbar.layoutParams = (toolbar.layoutParams as CollapsingToolbarLayout.LayoutParams).apply {
            topMargin = getStatusBarSize()
        }

        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = args.movie.title
        supportActionBar?.setHomeAsUpIndicator(R.drawable.ic_arrow)
    }

    private fun getStatusBarSize(): Int {
        val idStatusBarHeight = resources.getIdentifier("status_bar_height", "dimen", "android")
        return if (idStatusBarHeight > 0) resources.getDimensionPixelSize(idStatusBarHeight) else 0
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            onBackPressed()
        }
        return false
    }
}