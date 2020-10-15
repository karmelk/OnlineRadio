package com.onlinestation.fragment.favorite

import android.graphics.drawable.Drawable
import android.view.View
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onlinestation.entities.responcemodels.stationmodels.StationItemLocal
import com.onlinestation.fragment.favorite.viewmodel.FavoriteViewModel


@BindingAdapter("logo", "placeholder")
fun loadImage(view: AppCompatImageView, url: String?, placeHolder: Drawable) {
    Glide.with(view.context).load(url)
        .centerCrop()
        .placeholder(placeHolder)
        .circleCrop()
        .into(view)
}

@BindingAdapter("favorite_adapter", "view_model")
fun RecyclerView.setFavoriteAdapter(
    data: List<StationItemLocal>?,
    viewModel: FavoriteViewModel?
) {
    data?.let {
        adapter = FavoriteListStationAdapter(viewModel).apply { this.data = ArrayList(it) }
    }
}
