package com.onlinestation.fragment.searchradios

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

class SearchStationAdapter(
    val addRemoveStation: (item: StationItem) -> Unit,
    var playStation: (stationId: Int) -> Unit
) :  BaseAdapter<ViewBinding, StationItem, BaseViewHolder<StationItem, ViewBinding>>(
    DiffCallback()
) {

    @Suppress("UNCHECKED_CAST")
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

        override fun areContentsTheSame(oldItem: StationItem, newItem: StationItem): Boolean =
            oldItem == newItem
    }

}