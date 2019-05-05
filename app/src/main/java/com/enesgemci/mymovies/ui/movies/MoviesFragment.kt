package com.enesgemci.mymovies.ui.movies

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
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
import com.enesgemci.mymovies.data.Search
import com.enesgemci.mymovies.databinding.FragmentMoviesBinding
import com.enesgemci.mymovies.view.GridItemEqualOffsetDecoration
import com.enesgemci.mymovies.view.MaterialSearchBar
import com.enesgemci.mymovies.view.adapter.SuggestionsAdapter
import com.enesgemci.mymovies.viewmodel.MainViewModel
import com.github.ybq.endless.Endless

class MoviesFragment : BaseFragment<MainViewModel>(), MaterialSearchBar.OnSearchActionListener {

    override val viewModel: MainViewModel
            by lazy { ViewModelProviders.of(requireActivity(), viewModelFactory).get(MainViewModel::class.java) }

    private lateinit var recyclerView: RecyclerView
    private lateinit var warningText: TextView
    private lateinit var searchBar: MaterialSearchBar
    private lateinit var endless: Endless
    private var adapter: MoviesAdapter? = null

    private var currentPage: Int = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMoviesBinding.inflate(inflater, container, false).apply {
            searchable = true
        }
        searchBar = binding.searchBar
        warningText = binding.warningText
        recyclerView = binding.recyclerView

        setupSearchBar()
        setupRecyclerView()
        observeViewModel()

        return binding.root
    }

    private fun setupRecyclerView() {
        recyclerView.addItemDecoration(
            GridItemEqualOffsetDecoration(
                resources.getDimensionPixelOffset(R.dimen.grid_view_item_space)
            )
        )

        if (adapter == null) {
            adapter = MoviesAdapter(
                {
                    val direction = MoviesFragmentDirections.actionMoviesFragmentToMovieDetailsActivity(it)
                    findNavController().navigate(direction)
                },
                { movie, liked ->
                    if (liked) {
                        viewModel.saveMovie(movie)
                    } else {
                        viewModel.deleteMovie(movie)
                    }
                }
            )
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(context, 2).apply {
            spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
                override fun getSpanSize(position: Int): Int {
                    return if (position == adapter?.itemCount) {
                        spanCount
                    } else {
                        1
                    }
                }
            }
        }

        initInfiniteScroll()
    }

    private fun setupSearchBar() {
        viewModel.lastSearchLiveData.value
            ?.takeIf { it.isNotEmpty() }
            ?.let { searchBar.setPlaceHolder(it) }

        searchBar.addSuggestionsClickListener(object :
            SuggestionsAdapter.ItemInteractionListener {
            override fun onItemDeleted(position: Int, v: View?) {
                val suggestions = searchBar.lastSuggestions
                viewModel.deleteSearchQuery(suggestions[position].toString())
            }

            override fun onItemClicked(position: Int, v: View?) {
            }
        })
        searchBar.setOnSearchActionListener(this)
        searchBar.addTextChangeListener(object : TextWatcher {

            override fun afterTextChanged(s: Editable) {
                if (searchBar.isSearchEnabled) {
                    viewModel.getSearchPredictions(s.toString())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun observeViewModel() {
        viewModel.searchPredictionsLiveData.observe(viewLifecycleOwner, Observer {
            setPredictions(it)
        })

        viewModel.moviesLiveData.observe(viewLifecycleOwner, Observer { list ->

            viewModel.mergeMovies(list) { movies ->

                endless.loadMoreComplete()
                endless.isLoadMoreAvailable = viewModel.lastPageLiveData.value == false

                val hasMovies = movies.isNotEmpty()

                if (!hasMovies) {
                    warningText.text = getText(R.string.no_movie_with_search_warning)
                }

                warningText.isVisible = !hasMovies

                adapter?.submitList(movies)
            }
        })
    }

    override fun onButtonClicked(buttonCode: Int) {
    }

    override fun onSearchStateChanged(enabled: Boolean) {
        if (enabled) {
            viewModel.getLastSearches()
        }
    }

    private fun setPredictions(predictions: List<Search>) {
        if (searchBar.isSearchEnabled) {
            searchBar.updateLastSuggestions(predictions.map { it.title })
        }
    }

    override fun onSearchConfirmed(text: String) {
        if (viewModel.lastSearchLiveData.value != text) {
            Endless.remove(recyclerView)
            currentPage = 1
            adapter?.submitList(listOf())

            viewModel.saveLastSearchQuery(text)
            viewModel.searchMovies(text, currentPage)
        }
    }

    private fun initInfiniteScroll() {
        endless = Endless.applyTo(recyclerView, getLoadingView())
        endless.setAdapter(adapter)
        endless.setLoadMoreListener { page ->
            if (viewModel.lastPageLiveData.value == false) {
                currentPage = page
                viewModel.searchMovies(
                    viewModel.lastSearchQueryLiveData.value.orEmpty(),
                    page
                )
            }
        }
    }

    private fun getLoadingView() =
        View.inflate(requireContext(), R.layout.layout_infinite_loading, null).apply {
            layoutParams =
                ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
}