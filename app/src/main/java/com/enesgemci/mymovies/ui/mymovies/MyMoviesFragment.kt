package com.enesgemci.mymovies.ui.mymovies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.enesgemci.mymovies.R
import com.enesgemci.mymovies.adapter.MoviesAdapter
import com.enesgemci.mymovies.core.base.BaseFragment
import com.enesgemci.mymovies.databinding.FragmentMyMoviesBinding
import com.enesgemci.mymovies.view.GridItemEqualOffsetDecoration
import com.enesgemci.mymovies.viewmodel.MainViewModel

class MyMoviesFragment : BaseFragment<MainViewModel>() {

    override val viewModel: MainViewModel
            by lazy { ViewModelProviders.of(requireActivity(), viewModelFactory).get(MainViewModel::class.java) }

    private lateinit var warningText: TextView
    private lateinit var adapter: MoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMyMoviesBinding.inflate(inflater, container, false).apply {
            warningText.text = getString(R.string.no_movie_added_warning)
            warningText.isVisible = false
        }

        warningText = binding.warningText

        setupRecyclerView(binding.recyclerView)
        observeViewModel()

        return binding.root
    }

    private fun setupRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addItemDecoration(
            GridItemEqualOffsetDecoration(
                resources.getDimensionPixelOffset(R.dimen.grid_view_item_space)
            )
        )

        adapter = MoviesAdapter(
            {
                val direction = MyMoviesFragmentDirections.actionMyMoviesFragmentToMovieDetailsActivity2(it)
                findNavController().navigate(direction)
            },
            { movie, liked ->
                if (!liked) {
                    viewModel.deleteMovie(movie)
                }
            }
        )

        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == adapter.itemCount) {
                        spanCount
                    } else {
                        1
                    }
                }
            }
        }
    }

    private fun observeViewModel() {
        viewModel.savedMoviesLiveData.observe(viewLifecycleOwner, Observer {
            warningText.isVisible = it.isEmpty()
            adapter.submitList(it)
        })
    }
}