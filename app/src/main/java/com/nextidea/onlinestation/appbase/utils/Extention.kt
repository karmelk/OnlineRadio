package com.nextidea.onlinestation.appbase.utils

import androidx.lifecycle.LifecycleOwner
import com.nextidea.onlinestation.appbase.viewmodel.FlowObserver
import kotlinx.coroutines.flow.Flow

inline fun <reified T> Flow<T>.observeInLifecycle(
    lifecycleOwner: LifecycleOwner
) = FlowObserver(lifecycleOwner, this)
