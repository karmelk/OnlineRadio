package com.nextidea.onlinestation.fragment.topstations

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.nextidea.onlinestation.appbase.adapter.BaseAdapter
import com.nextidea.onlinestation.databinding.ItemStationBinding
import com.nextidea.onlinestation.domain.entities.StationItem
import com.nextidea.onlinestation.utils.loadImageCircle
import com.nextidea.onlinestation.utils.stationIsFavorite
import com.nextidea.onlinestation.appbase.adapter.BaseViewHolder
import com.nextidea.onlinestation.appbase.adapter.ViewTypeLoading
import com.nextidea.onlinestation.databinding.ItemLoadingBinding

class TopStationAdapter(
    var addRemoveStation: (item: StationItem) -> Unit,
    var playStation: (stationId: Int) -> Unit
) : BaseAdapter<ViewBinding, StationItem, BaseViewHolder<StationItem, ViewBinding>>(
    ItemsDiffCallback { oldItem, newItem -> oldItem.id == newItem.id }
) {
    private var isLoading = true

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<StationItem, ViewBinding> {
        return when (viewType) {
            ViewTypeLoading.VIEW_TYPE_DATA.type -> MyViewHolder(
                ItemStationBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
            else -> LoadingViewHolder(
                ItemLoadingBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
            )
        }
    }

    private inner class MyViewHolder(val binding: ItemStationBinding) :
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

    private inner class LoadingViewHolder(binding: ItemLoadingBinding) :
        BaseViewHolder<StationItem, ViewBinding>(binding) {
        override fun bind(item: StationItem, context: Context) {}
    }

    override fun getItemViewType(position: Int): Int {
        return if (itemCount - 1 == position  && isLoading)
            ViewTypeLoading.VIEW_TYPE_LOADING.type
        else
            ViewTypeLoading.VIEW_TYPE_DATA.type
    }

    fun onLoadingState(progress:Boolean) {
        isLoading = progress
    }
}