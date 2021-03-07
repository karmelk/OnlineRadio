package com.kmworks.appbase.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {


    private val _loadStation by lazy { MutableLiveData<String>() }
    val loadStation: LiveData<String> get() = _loadStation

    fun loadData(mediaId: Long) {
        _loadStation.value = mediaId.toString()
    }
}