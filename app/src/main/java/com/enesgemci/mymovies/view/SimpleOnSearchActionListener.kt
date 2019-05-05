package com.enesgemci.mymovies.view

/**
 * taken from : https://github.com/mancj/MaterialSearchBar
 * modified by enes gemci
 */
abstract class SimpleOnSearchActionListener : MaterialSearchBar.OnSearchActionListener {

    override fun onSearchStateChanged(enabled: Boolean) {
    }

    override fun onSearchConfirmed(text: String) {
    }

    override fun onButtonClicked(buttonCode: Int) {
    }
}
