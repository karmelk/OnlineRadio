package com.onlinestation.fragment.topstations

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.onlinestation.appbase.adapter.BaseAdapter
import com.onlinestation.appbase.adapter.BaseViewHolder
import com.onlinestation.databinding.ItemStationBinding
import com.onlinestation.domain.entities.StationItem
import com.onlinestation.utils.loadImageCircle
import com.onlinestation.utils.stationIsFavorite

internal class TopStationAdapter(
    var addRemoveStation: (item: StationItem) -> Unit,
    var playStation: (stationId: Int) -> Unit
) : BaseAdapter<ViewBinding, StationItem, BaseViewHolder<StationItem, ViewBinding>>(
    DiffCallback()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<StationItem, ViewBinding> = MyViewHolder(
        ItemStationBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    private inner class MyViewHolder(val binding: ItemStationBinding) :
        BaseViewHolder<StationItem, ViewBinding>(binding) {
        override fun bind(item: StationItem) {
            with(binding) {
                stationLogo.loadImageCircle(item.icon)
                favorite.stationIsFavorite(item.isFavorite)
                stationName.text = item.name
                genreName.text = item.genre
                favorite.setOnClickListener {
                    addRemoveStation(item)
                }
            }
        }

        override fun onItemClick(item: StationItem) {
            playStation(item.id)
        }
    }

    private class DiffCallback : DiffUtil.ItemCallback<StationItem>() {
        override fun areItemsTheSame(oldItem: StationItem, newItem: StationItem): Boolean =
            oldItem.id == newItem.id

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: StationItem, newItem: StationItem): Boolean =
            oldItem == newItem

    }
}