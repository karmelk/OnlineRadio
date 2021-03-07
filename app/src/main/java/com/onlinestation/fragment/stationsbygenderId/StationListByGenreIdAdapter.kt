package com.onlinestation.fragment.stationsbygenderId

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.kmworks.appbase.adapter.BaseAdapter
import com.kmworks.appbase.adapter.BaseViewHolder
import com.onlinestation.R
import com.onlinestation.databinding.ItemStationBinding
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItem




class StationListByGenreIdAdapter : BaseAdapter<ViewBinding, StationItem, BaseViewHolder<StationItem, ViewBinding>>(
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

    internal inner class MyViewHolder(val binding: ItemStationBinding) :
        BaseViewHolder<StationItem, ViewBinding>(binding) {
        override fun bind(item: StationItem) {
            binding.run {
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

                /*binding.favorite.setOnClickListener {
                    removeFavoriteItem(item.id)
                }*/
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
}