package com.onlinestation.fragment.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onlinestation.R
import com.onlinestation.databinding.ItemStationFavoriteBinding
import com.onlinestation.entities.responcemodels.stationmodels.StationItemLocal
import kotlinx.android.synthetic.main.item_station_favorite.view.*

class FavoriteListStationAdapter(
    var itemList: MutableList<StationItemLocal>,
    var removeFavorite: (itemId: Int) -> Unit
) : RecyclerView.Adapter<FavoriteListStationAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder = MyViewHolder(
        DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_station_favorite,
            parent,
            false
        )
    )


    override fun getItemCount(): Int =itemList.size

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemStationBinding.itemStation = itemList[position]
    }

    inner class MyViewHolder(
        val itemStationBinding: ItemStationFavoriteBinding
    ) : RecyclerView.ViewHolder(itemStationBinding.root)

    fun updateList(updateList: MutableList<StationItemLocal>?) {
        updateList?.let {
            itemList = it
            notifyDataSetChanged()
        }
    }

}