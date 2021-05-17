package com.blogspot.soyamr.weathermap.presentation.utils.extentions

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<MutableList<T>>.add(item: T) {
    val oldValue = this.value as ArrayList
    oldValue.add(item)
    this.value = oldValue
}

fun <T> MutableLiveData<MutableList<T>>.clear() {
    val oldValue = this.value as ArrayList
    oldValue.clear()
    this.value = oldValue
}

