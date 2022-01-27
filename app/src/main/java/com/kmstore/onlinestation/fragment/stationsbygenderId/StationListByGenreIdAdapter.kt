package com.kmstore.onlinestation.fragment.stationsbygenderId

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.kmstore.onlinestation.appbase.adapter.BaseAdapter
import com.kmstore.onlinestation.appbase.adapter.BaseViewHolder
import com.kmstore.onlinestation.appbase.adapter.ViewTypeLoading
import com.kmstore.onlinestation.databinding.ItemLoadingBinding
import com.kmstore.onlinestation.databinding.ItemStationBinding
import com.kmstore.onlinestation.domain.entities.StationItem
import com.kmstore.onlinestation.utils.loadImageCircle
import com.kmstore.onlinestation.utils.stationIsFavorite

class StationListByGenreIdAdapter(
    val addRemoveStation: (item: StationItem) -> Unit,
    var playStation: (stationId: Int) -> Unit
) : BaseAdapter<ViewBinding, StationItem, BaseViewHolder<StationItem, ViewBinding>>(
        ItemsDiffCallback { oldItem, newItem -> oldItem.id == newItem.id }
    ) {

    private var isLoading = true

    @Suppress("UNCHECKED_CAST")
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
        return if (itemCount - 1 == position && isLoading)
            ViewTypeLoading.VIEW_TYPE_LOADING.type
        else
            ViewTypeLoading.VIEW_TYPE_DATA.type
    }

    fun onLoadingState(progress: Boolean) {
        isLoading = progress
    }

}