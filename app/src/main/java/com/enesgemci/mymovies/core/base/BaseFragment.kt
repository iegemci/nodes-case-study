package com.enesgemci.mymovies.core.base

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.enesgemci.mymovies.core.view.MProgressDialog
import com.google.android.material.snackbar.Snackbar
import dagger.android.support.AndroidSupportInjection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.properties.Delegates

abstract class BaseFragment<VM : BaseViewModel> : Fragment(), CoroutineScope {

    private var progressDialog: MProgressDialog by Delegates.notNull()

    /**
     * This is the job for all coroutines started by this Fragment.
     *
     * Cancelling this job will cancel all coroutines started by this Fragment.
     */
    private val fragmentJob = Job()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + fragmentJob

    abstract val viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        progressDialog = MProgressDialog(requireContext())
    }

    override fun onAttach(context: Context) {
        AndroidSupportInjection.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.loading.observe(this, Observer { loading ->
            if (loading) {
                showLoadingDialog()
            } else {
                dismissLoadingDialog()
            }
        })

        viewModel.error.observe(this, Observer { e ->
            showMessage(e.message.orEmpty())
        })
    }

    override fun onDestroyView() {
        fragmentJob.cancel()

        dismissLoadingDialog()

        super.onDestroyView()
    }

    private fun showLoadingDialog() {
        progressDialog.show()
    }

    private fun dismissLoadingDialog() {
        progressDialog.dismiss()
    }

    private fun showMessage(message: String) {
        view?.let { Snackbar.make(it, message, Snackbar.LENGTH_LONG).show() }
    }
}
