package com.nextidea.onlinestation.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.CheckResult
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.textfield.TextInputLayout
import com.nextidea.onlinestation.R
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.onStart
import java.util.ArrayList


fun <T> debounce(
    waitMs: Long = 300L,
    scope: CoroutineScope,
    destinationFunction: (T) -> Unit
): (T) -> Unit {
    var debounceJob: Job? = null
    return { param: T ->
        debounceJob?.cancel()
        debounceJob = scope.launch {
            delay(waitMs)
            destinationFunction(param)
        }
    }
}

@ExperimentalCoroutinesApi
@CheckResult
fun SearchView.textChanges(): Flow<CharSequence?> {
    return callbackFlow<CharSequence?> {
        val listener = object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                trySend(query).isSuccess
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                trySend(newText).isSuccess
                return true
            }
        }
        setOnQueryTextListener(listener)
     //   addTextChangedListener(listener)
      //  awaitClose { removeTextChangedListener(listener) }
        awaitClose {  }
    }.onStart { emit(query) }
}

inline fun <reified T : View> Fragment.find(id: Int): T = view?.findViewById(id) as T
inline fun <reified T : View> Fragment.findOptional(id: Int): T? = view?.findViewById(id) as? T


fun Fragment.dip(value: Int): Int = (value * resources.displayMetrics.density).toInt()
fun Fragment.dip(value: Float): Int = (value * resources.displayMetrics.density).toInt()

fun <T : Fragment> T.withArguments(vararg params: Pair<String, Any>): T {
    arguments = bundleOf(*params)
    return this
}

fun Fragment.toast(message: CharSequence) =
    Toast.makeText(this.context, message, Toast.LENGTH_SHORT).show()

fun Fragment.toast(resourceId: Int) =
    Toast.makeText(this.context, resourceId, Toast.LENGTH_SHORT).show()

@Suppress("unused")
fun sharedElementsToArray(map: Map<View, String>): Array<androidx.core.util.Pair<View, String>> {
    val pairList = ArrayList<androidx.core.util.Pair<View, String>>()

    for ((k, v) in map) {
        val pair = androidx.core.util.Pair<View, String>(k, v)
        pairList.add(pair)
    }

    return pairList.toTypedArray()
}

@Suppress("unused")
fun emptySharedElements(): Array<androidx.core.util.Pair<View, String>> {
    val pairList = ArrayList<androidx.core.util.Pair<View, String>>()
    return pairList.toTypedArray()
}

@Suppress("unused")
fun fadeOutView(
    view: View,
    delay: Long = 0,
    endVisibility: Int = View.GONE,
    duration: Long? = null,
    endAlpha: Float = 0f,
    endAction: (() -> Unit)? = null
) {
    if (view.visibility == View.VISIBLE) {
        val animationDuration =
            duration ?: 1L * view.resources.getInteger(android.R.integer.config_shortAnimTime)
        view.clearAnimation()
        view.animate()
            .alpha(endAlpha)
            .setDuration(animationDuration)
            .setStartDelay(delay)
            .withEndAction {
                view.visibility = endVisibility
                endAction?.invoke()
            }
            .start()
    }
}

@Suppress("unused")
fun View.fadeOut(
    delay: Long = 0,
    endVisibility: Int = View.GONE,
    duration: Long? = null,
    endAction: (() -> Unit)? = null
) =
    fadeOutView(this, delay, endVisibility, duration, 0f, endAction)


@Suppress("unused")
fun fadeIn(
    view: View,
    delay: Long = 0,
    duration: Long? = null,
    targetAlpha: Float = 1f,
    startAlpha: Float = 0f,
    ignoreCurrentVisibility: Boolean = false
) {
    if (view.visibility != View.VISIBLE || ignoreCurrentVisibility) {
        val animationDuration =
            duration ?: 1L * view.resources.getInteger(android.R.integer.config_mediumAnimTime)

        view.clearAnimation()
        view.alpha = startAlpha
        view.animate()
            .alpha(targetAlpha)
            .setDuration(animationDuration)
            .setStartDelay(delay)
            .withStartAction { view.visibility = View.VISIBLE }
            .start()
    } else {
        view.alpha = 1f
    }
}


fun Activity.hideKeyboard(viewToHide: View?) {
    viewToHide?.let {
        val imm = this
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(it.windowToken, 0)
    }
}


fun View.show() {
    if (visibility != View.VISIBLE) {
        visibility = View.VISIBLE
    }
}

fun View.showIf(condition: Boolean) =
    when (condition) {
        true -> show()
        else -> gone()
    }

fun View.makeInvisibleIf(condition: Boolean) =
    when (condition) {
        true -> invisible()
        else -> show()
    }


fun View.showIf(conditionFn: () -> Boolean) =
    when (conditionFn()) {
        true -> show()
        else -> gone()
    }

fun View.visibleIf(condition: Boolean) =
    when (condition) {
        true -> show()
        else -> invisible()
    }

fun View.visibleIf(conditionFn: () -> Boolean) =
    when (conditionFn()) {
        true -> show()
        else -> invisible()
    }

fun TextView.show(text: String) {
    setText(text)
    show()
}

fun TextView.show(text: CharSequence) {
    setText(text)
    show()
}

fun TextView.showIfNotEmpty(text: String?) = when {
    text.isNullOrEmpty() -> gone()
    else -> {
        this.text = text
        show()
    }
}

fun View.showIfNotEmpty(text: String?) = when {
    text.isNullOrEmpty() -> gone()
    else -> show()
}

fun TextView.showIfNotEmpty(text: CharSequence?) = when {
    text.isNullOrEmpty() -> gone()
    else -> {
        this.text = text
        show()
    }
}

fun TextView.showIf(text: String?, condition: Boolean) =
    when (condition) {
        true -> showIfNotEmpty(text)
        else -> gone()
    }

fun TextView.showIf(text: CharSequence?, condition: Boolean) =
    when (condition) {
        true -> showIfNotEmpty(text)
        else -> gone()
    }

fun View.isVisible() = this.visibility == View.VISIBLE
fun View.isNotVisible() = this.visibility != View.VISIBLE

fun View.gone() {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
}

fun View.invisible() {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
}

fun View.fadeIn(targetAlpha: Float = 1f) {
    fadeIn(this, targetAlpha = targetAlpha)
}

fun View.fadeOut() {
    fadeOutView(this)
}

@SuppressLint("NewApi")
fun TextView.showHtml(text: String?) {
    val htmlText = text?.replace("\n", "<br>") ?: ""

    this.text = when (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        true -> Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
        false -> Html.fromHtml(htmlText)
    }

    this.movementMethod = LinkMovementMethod.getInstance()
}

fun TextView.showTextAsLink(textId: Int, link: String?) {
    showTextAsLink(this.context.getString(textId), link)
}

fun TextView.showTextAsLink(text: String?, link: String?) {
    if (TextUtils.isEmpty(text)) return
    if (TextUtils.isEmpty(link)) return

    val html = "<a href=\"$link\">$text</a>"
    showHtml(html)
}

//.................................................................................................

private fun slideAnimator(layout: View, start: Int, end: Int): ValueAnimator {
    val animator = ValueAnimator.ofInt(start, end)

    val listener = ValueAnimator.AnimatorUpdateListener { animation ->
        val height = animation.animatedValue as Int
        val layoutParams = layout.layoutParams
        layoutParams.height = height
        layout.layoutParams = layoutParams
    }

    animator.addUpdateListener(listener)
    return animator
}

fun View.expand(onAnimationEnd: (() -> Unit)? = null) {
    if (isVisible()) {
        onAnimationEnd?.invoke()
        return
    }

    show()

    val widthSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    val heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
    measure(widthSpec, heightSpec)

    val animator = slideAnimator(this, 0, measuredHeight)
    animator.addListener(
        object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                val layoutParams = this@expand.layoutParams
                layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
                this@expand.layoutParams = layoutParams

                onAnimationEnd?.invoke()
            }
        }
    )

    animator.start()
}

fun View.collapse(onAnimationEnd: (() -> Unit)? = null) {
    if (!isVisible()) {
        onAnimationEnd?.invoke()
        return
    }

    val height = height
    val animator = slideAnimator(this, height, 0)
    animator.addListener(
        object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator?) {
                gone()
                onAnimationEnd?.invoke()
            }
        }
    )

    animator.start()
}

fun ImageView.show(imageId: Int?) = when (imageId) {
    null -> invisible()
    else -> {
        setImageResource(imageId)
        show()
    }
}

fun ImageView.show(bitmap: Bitmap?) = when (bitmap) {
    null -> invisible()
    else -> {
        setImageBitmap(bitmap)
        show()
    }
}

fun ImageView.show(drawable: Drawable?) = when (drawable) {
    null -> invisible()
    else -> {
        setImageDrawable(drawable)
        show()
    }
}

fun ImageView.loadImageCircle(url: String) {
    Glide.with(this)
        .load(url)
        .centerCrop()
        .circleCrop()
        .apply(
            RequestOptions()
                .placeholder(R.drawable.ic_default_radio)
                .error(R.drawable.ic_default_radio)
        )
        .into(this)
}

fun ImageView.stationIsFavorite(isFavorite: Boolean) {
    if (isFavorite) {
        this.setImageDrawable(
            ContextCompat.getDrawable(
                this.context,
                R.drawable.ic_favorite_selected_24dp
            )
        )
    } else {
        this.setImageDrawable(
            ContextCompat.getDrawable(
                this.context,
                R.drawable.ic_favorite_unselected_24dp
            )
        )
    }
}

fun TextInputLayout.showError(errorMessage: String?) {
    when {
        errorMessage.isNullOrBlank() -> clearError()
        else -> error = errorMessage
    }
}

// В случае, если одновременно используются helperText и error, при простом присваивании
// error := null значение helperText через пару таких присваиваний делается невидимым.
// Должны починить в 1.3.0
// https://github.com/material-components/material-components-android/issues/621
// Пока вместо error := null явно вызываем запрет отображения сообщения об ошибке.
fun TextInputLayout.clearError() {
    isErrorEnabled = false
}

fun MenuItem.show() {
    isVisible = true
}

fun MenuItem.gone() {
    isVisible = false
}