package com.onlinestation.appbase.utils

import android.app.Activity
import android.os.Build
import androidx.core.content.PermissionChecker


fun Activity.checkPermissionsGranted(permissions: ArrayList<String>): Boolean {
    permissions.forEach {
        if ((PermissionChecker.checkCallingOrSelfPermission(
                this,
                it
            ) != PermissionChecker.PERMISSION_GRANTED)
        )
            return false
    }
    return true
}

fun Activity.hasPermission(permission: String): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        return PermissionChecker.checkSelfPermission(
            this,
            permission
        ) == PermissionChecker.PERMISSION_GRANTED
    }
    return true
}


