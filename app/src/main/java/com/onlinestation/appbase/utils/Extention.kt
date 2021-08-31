package com.onlinestation.appbase.utils

import android.app.Activity
import android.os.Build
import androidx.core.content.PermissionChecker
import androidx.lifecycle.LifecycleOwner
import com.onlinestation.appbase.viewmodel.FlowObserver
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach

inline fun <reified T> Flow<T>.observeInLifecycle(
    lifecycleOwner: LifecycleOwner
) = FlowObserver(lifecycleOwner, this)

inline fun <reified T> Flow<T>.onEachOwner(
    crossinline action: (T) -> Unit,
) {
    onEach {action(it ?: return@onEach)}
}

