package com.onlinestation.appbase.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding


abstract class BaseAdapter<ItemViewBinding : ViewBinding, Item, ViewHolder : BaseViewHolder<Item, ItemViewBinding>>(
    diffUtilCallBack: DiffUtil.ItemCallback<Item>
) : ListAdapter<Item, ViewHolder>(diffUtilCallBack) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            getItem(position)?.let {item->
                bind(item)
                itemView.setOnClickListener {
                    if (position <= itemCount - 1)
                        onItemClick(item)
                }
            }
        }
    }

}