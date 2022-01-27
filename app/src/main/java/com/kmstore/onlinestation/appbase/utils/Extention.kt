package com.kmstore.onlinestation.appbase.utils

import androidx.lifecycle.LifecycleOwner
import com.kmstore.onlinestation.appbase.viewmodel.FlowObserver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

inline fun <reified T> Flow<T>.observeInLifecycle(
    lifecycleOwner: LifecycleOwner
) = FlowObserver(lifecycleOwner, this)
