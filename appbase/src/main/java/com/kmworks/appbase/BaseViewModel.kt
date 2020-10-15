package com.kmworks.appbase

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.kmworks.appbase.command.SingleLiveData
import com.kmworks.appbase.command.ViewCommand

abstract class BaseViewModel : ViewModel(){

    protected val _command = SingleLiveData<ViewCommand>()
    val command: LiveData<ViewCommand>
        get() = _command
}