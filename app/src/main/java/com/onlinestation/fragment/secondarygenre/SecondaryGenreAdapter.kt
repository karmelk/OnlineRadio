package com.onlinestation.fragment.secondarygenre

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.onlinestation.R
import com.onlinestation.entities.responcemodels.gendermodels.SecondaryGenreItem
import kotlinx.android.synthetic.main.item_genre.view.*

class SecondaryGenreAdapter(var itemList: MutableList<SecondaryGenreItem>, val clickGenreItem: (genreId:Int)->Unit) :
    RecyclerView.Adapter<SecondaryGenreAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_genre, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        holder.bindItem(itemList[position])


    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bindItem(item: SecondaryGenreItem) {
            with(itemView) {
                genreName.text = item.name
                stationsCount.text = "Stations"+item.count
                itemView.setOnClickListener { clickGenreItem(itemList[adapterPosition].id) }

            }
        }

    }

    fun updateList(updateList: MutableList<SecondaryGenreItem>?) {
        updateList?.let {
            itemList = it
            notifyDataSetChanged()
        }

    }

}