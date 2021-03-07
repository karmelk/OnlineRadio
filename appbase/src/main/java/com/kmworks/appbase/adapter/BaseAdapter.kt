package com.kmworks.appbase.adapter

import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.viewbinding.ViewBinding


abstract class BaseAdapter< ItemViewBinding : ViewBinding,Item, ViewHolder : BaseViewHolder<Item, ItemViewBinding>>(
    diffUtilCallBack: DiffUtil.ItemCallback<Item>
) : ListAdapter<Item,ViewHolder>(diffUtilCallBack) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder) {
            bind(getItem(position))
            itemView.setOnClickListener {
                onItemClick(getItem(position))
            }
        }
    }

}