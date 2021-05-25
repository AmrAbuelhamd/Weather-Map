package com.blogspot.soyamr.weathermap.presentation.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.blogspot.soyamr.data.utils.NoInternetException
import com.blogspot.soyamr.weathermap.R
import kotlinx.coroutines.CoroutineExceptionHandler

open class BaseViewModel : ViewModel() {

    protected val _errorMessage: MutableLiveData<Int> = MutableLiveData(0)
    val errorMessage: LiveData<Int> = _errorMessage

    protected val _progressBarVisibility: MutableLiveData<Boolean> = MutableLiveData(false)
    val progressBarVisibility: LiveData<Boolean> = _progressBarVisibility

    val handler = CoroutineExceptionHandler { _, exception ->
        if (exception is NoInternetException)
            _errorMessage.postValue(R.string.no_internet)
        else
            _errorMessage.postValue(R.string.something_went_wrong)
        println(" : "+exception)
    }

    fun switchProgressBarVisibility(visibility: Boolean) {
        _progressBarVisibility.value = visibility
    }
}