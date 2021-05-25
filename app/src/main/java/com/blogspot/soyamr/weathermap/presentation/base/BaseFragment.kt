package com.blogspot.soyamr.weathermap.presentation.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<T : BaseViewModel, DB : ViewDataBinding>(
    @LayoutRes private val layoutResId: Int
) : Fragment() {

    protected open lateinit var viewModel: T

    lateinit var binding: DB

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return DataBindingUtil.inflate<DB>(inflater, layoutResId, container, false).also {
            binding = it
            it.lifecycleOwner = this
            setViewModel()
            viewModel.errorMessage.observe(viewLifecycleOwner, ::showError)
        }.root
    }

    abstract fun setViewModel()

    private fun showError(errorStringId: Int?) {
        errorStringId?.let {
            if (it != 0) {
                showMessage(it, false)
            }
        }
    }

    protected fun showMessage(msgId: Int, showProgressBar: Boolean) {
        viewModel.switchProgressBarVisibility(showProgressBar)
        Toast.makeText(
            requireContext(), getString(msgId), Toast.LENGTH_SHORT
        ).show()
    }
}