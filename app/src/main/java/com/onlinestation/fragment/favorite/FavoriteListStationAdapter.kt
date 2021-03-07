package com.onlinestation.fragment.favorite

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kmworks.appbase.adapter.BaseAdapter
import com.kmworks.appbase.adapter.BaseViewHolder
import com.onlinestation.R
import com.onlinestation.databinding.ItemGenreBinding
import com.onlinestation.databinding.ItemStationFavoriteBinding
import com.onlinestation.entities.localmodels.GenderItem
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItem


class FavoriteListStationAdapter :
    BaseAdapter<ViewBinding, StationItem, BaseViewHolder<StationItem, ViewBinding>>(
        DiffCallback()
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<StationItem, ViewBinding> =
        MyViewHolder(
            ItemStationFavoriteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )


    inner class MyViewHolder(private val binding: ItemStationFavoriteBinding) :
        BaseViewHolder<StationItem, ViewBinding>(binding) {

        override fun bind(item: StationItem) {

            with(binding) {
                if (item.isFavorite) {
                    favorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            root.context,
                            R.drawable.ic_favorite_selected_24dp
                        )
                    )
                } else {
                    favorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            root.context,
                            R.drawable.ic_favorite_unselected_24dp
                        )
                    )
                }
            }

        }
    }
}

internal class DiffCallback : DiffUtil.ItemCallback<StationItem>() {
    override fun areItemsTheSame(oldItem: StationItem, newItem: StationItem): Boolean =
        oldItem.id == newItem.id

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: StationItem, newItem: StationItem): Boolean =
        oldItem == newItem

    override fun getChangePayload(oldItem: StationItem, newItem: StationItem): Any? {
        return super.getChangePayload(oldItem, newItem)
    }
}

