package com.onlinestation.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.NavHostFragment

fun hideKeyboard(activity: Activity, viewToHide: View?) {
    viewToHide?.let {
        val imm = activity
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

inline fun <reified F> getCurrentFragment(navHostFragment: NavHostFragment): F? {
    return if (navHostFragment.isAdded && navHostFragment.childFragmentManager.fragments.size > 0) {
        if (navHostFragment.childFragmentManager.fragments[0] is F) {
            navHostFragment.childFragmentManager.fragments[0] as F
        } else {
            null
        }
    } else null
}