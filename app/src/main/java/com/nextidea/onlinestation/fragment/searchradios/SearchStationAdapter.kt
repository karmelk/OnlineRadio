package com.nextidea.onlinestation.fragment.searchradios

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.nextidea.onlinestation.appbase.adapter.BaseAdapter
import com.nextidea.onlinestation.appbase.adapter.BaseViewHolder
import com.nextidea.onlinestation.databinding.ItemStationBinding
import com.nextidea.onlinestation.domain.entities.StationItem
import com.nextidea.onlinestation.utils.loadImageCircle
import com.nextidea.onlinestation.utils.stationIsFavorite

class SearchStationAdapter(
    val addRemoveStation: (item: StationItem) -> Unit,
    var playStation: (stationId: Int) -> Unit
) :  BaseAdapter<ViewBinding, StationItem, BaseViewHolder<StationItem, ViewBinding>>(
    ItemsDiffCallback { oldItem, newItem -> oldItem.id == newItem.id }
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
        override fun bind(item: StationItem, context: Context) {
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

}