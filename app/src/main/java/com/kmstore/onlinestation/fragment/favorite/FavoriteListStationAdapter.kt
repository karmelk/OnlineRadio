package com.kmstore.onlinestation.fragment.favorite

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.kmstore.onlinestation.appbase.adapter.BaseAdapter
import com.kmstore.onlinestation.appbase.adapter.BaseViewHolder
import com.kmstore.onlinestation.databinding.ItemStationBinding
import com.kmstore.onlinestation.domain.entities.StationItem
import com.kmstore.onlinestation.utils.loadImageCircle
import com.kmstore.onlinestation.utils.stationIsFavorite


class FavoriteListStationAdapter(
    val addRemoveStation: (item: StationItem) -> Unit,
    var playStation: (stationId: Int) -> Unit
) :
    BaseAdapter<ViewBinding, StationItem, BaseViewHolder<StationItem, ViewBinding>>(
        ItemsDiffCallback { oldItem, newItem -> oldItem.id == newItem.id }
    ) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<StationItem, ViewBinding> =
        MyViewHolder(
            ItemStationBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    private inner class MyViewHolder(private val binding: ItemStationBinding) :
        BaseViewHolder<StationItem, ViewBinding>(binding) {

        override fun bind(item: StationItem, context: Context) {

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

}

