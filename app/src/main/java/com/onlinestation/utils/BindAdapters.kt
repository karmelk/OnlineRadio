package com.onlinestation.utils

import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.onlinestation.R

@BindingAdapter("logo", "placeholder")
fun loadImage(view: AppCompatImageView, url: String?, placeHolder: Drawable) {
    Glide.with(view.context).load(url)
        .placeholder(placeHolder)
        .into(view)
}