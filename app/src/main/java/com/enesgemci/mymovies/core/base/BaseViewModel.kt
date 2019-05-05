package com.enesgemci.mymovies.core.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class BaseViewModel : ViewModel(), CoroutineScope {

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    val loading = MutableLiveData<Boolean>()

    val error = MutableLiveData<Throwable>()

    /**
     * Cancel all coroutines when the ViewModel is cleared.
     */
    @ExperimentalCoroutinesApi
    override fun onCleared() {
        super.onCleared()
        cancel()
    }
}
