package com.onlinestation.fragment.favorite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.BindingMethod
import androidx.databinding.BindingMethods
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.onlinestation.R
import com.onlinestation.databinding.ItemStationFavoriteBinding
import com.onlinestation.entities.responcemodels.stationmodels.StationItemLocal
import com.onlinestation.fragment.favorite.viewmodel.FavoriteViewModel
import com.onlinestation.utils.DiffUtilCallBack

class FavoriteListStationAdapter(private val viewModel: FavoriteViewModel?) : ListAdapter<StationItemLocal,FavoriteListStationAdapter.MyViewHolder>(DiffUtilCallBack()) {

    var data: MutableList<StationItemLocal> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val bind: ItemStationFavoriteBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.item_station_favorite,
            parent,
            false
        )
        bind.adapter=this
        return MyViewHolder(bind)

    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.itemStationBinding.itemStation = data.get(position)
    }

    inner class MyViewHolder(
        val itemStationBinding: ItemStationFavoriteBinding
    ) : RecyclerView.ViewHolder(itemStationBinding.root)


    fun removeItem(itemId: Int) {
        viewModel?.removeFavoriteItem(itemId)
        data.find { it.id == itemId }?.run {
            data.remove(this)
            notifyDataSetChanged()
        }
    }

}