package com.onlinestation.adapter

import android.graphics.drawable.Drawable
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.onlinestation.R
import com.onlinestation.entities.localmodels.GenderItem
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItem

import com.onlinestation.fragment.favorite.FavoriteListStationAdapter
import com.onlinestation.fragment.favorite.viewmodel.FavoriteViewModel

@BindingAdapter("add_favorite")
fun AppCompatImageView.isFavorite(
    isFavorite: Boolean
) {
    if (isFavorite) {
        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_selected_24dp))
    } else {
        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_favorite_unselected_24dp))
    }
}

@BindingAdapter("play_pause")
fun AppCompatImageView.isChangeIcon(
    isFavorite: Boolean
) {
    if (isFavorite) {
        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_pause_bottom_panel))
    } else {
        setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_play_bottom_panel))
    }
}

@BindingAdapter("image_source")
fun AppCompatImageView.imageSource(uri: String) {
    Glide.with(context)
        .load(uri)
        .centerCrop()
        .apply(
            RequestOptions()
                .placeholder(R.drawable.ic_default_radio)
                .error(R.drawable.ic_default_radio)
        )
        .into(this)
}

@BindingAdapter("image_source_circle")
fun AppCompatImageView.imageSourceCircle(uri: String) {
    Glide.with(context)
        .load(uri)
        .centerCrop()
        .circleCrop()
        .apply(
            RequestOptions()
                .placeholder(R.drawable.ic_default_radio)
                .error(R.drawable.ic_default_radio)
        )
        .into(this)
}

@BindingAdapter("logo", "placeholder")
fun loadImage(view: AppCompatImageView, url: String?, placeHolder: Drawable) {
    Glide.with(view.context).load(url)
        .circleCrop()
        .placeholder(placeHolder)
        .into(view)
}


