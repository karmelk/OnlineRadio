package com.nextidea.onlinestation.appbase.adapter

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding


abstract class BaseAdapter<ItemViewBinding : ViewBinding, Item, ViewHolder : BaseViewHolder<Item, ItemViewBinding>>(
    diffUtilCallBack: DiffUtil.ItemCallback<Item>
) : ListAdapter<Item, ViewHolder>(diffUtilCallBack) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            getItem(position)?.let {item->
                bind(item, itemView.context)
                itemView.setOnClickListener {
                    if (position <= itemCount - 1)
                        onItemClick(item)
                }
            }
        }
    }

    protected class ItemsDiffCallback<Item>(private val callback: (Item, Item) -> Boolean) : DiffUtil.ItemCallback<Item>() {

        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean =
            callback(oldItem, newItem)

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean =
            oldItem == newItem

    }

}