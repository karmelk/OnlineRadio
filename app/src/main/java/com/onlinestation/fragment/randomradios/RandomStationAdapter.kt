package com.onlinestation.fragment.randomradios

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.onlinestation.R
import com.onlinestation.entities.responcemodels.stationmodels.server.StationItem
import kotlinx.android.synthetic.main.item_station.view.*
import java.util.*

class RandomStationAdapter(
    var itemList: MutableList<StationItem>,
    var addFavorite: (item: StationItem) -> Unit,
    var removeFavorite: (item: StationItem) -> Unit,
    var playStation: (stationId: Int) -> Unit
) :
    RecyclerView.Adapter<RandomStationAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_station, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bindItem(itemList[position])
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(item: StationItem) {
            with(itemView) {
                stationName.text = item.name
              /*  stationsCount.text = "Stations" + item.genre
                Glide.with(context)
                    .load(item.logo)
                    .circleCrop()
                    .placeholder(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_default_station
                        )
                    )
                    .into(stationLogo)*/
                favorite.setOnClickListener {
                    if (!item.isFavorite) {
                        addFavorite(item)
                    } else {
                        removeFavorite(item)
                    }
                }
                if (item.isFavorite) {
                    favorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_favorite_selected_24dp
                        )
                    )
                } else {
                    favorite.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.ic_favorite_unselected_24dp
                        )
                    )
                }
                itemView.setOnClickListener {
                   // playStation.invoke(item.id)
                }

            }
        }
    }

    fun updateList(updateList: MutableList<StationItem>?) {
        updateList?.let {
            itemList = it
            notifyDataSetChanged()
        }

    }

    fun updateErrorItem(data: StationItem) {
        itemList.find { it.id == data.id }?.isFavorite = data.isFavorite
        notifyDataSetChanged()
    }

    fun removeFavoriteItem(data: StationItem) {
        itemList.find { it.id == data.id }?.apply {
            isFavorite = false
         //   createDateTime = 0
        }
        notifyDataSetChanged()
    }

    fun updateSuccessItem(data: StationItem) {
        itemList.find { it.id == data.id }?.isFavorite = data.isFavorite
        notifyDataSetChanged()
    }

}