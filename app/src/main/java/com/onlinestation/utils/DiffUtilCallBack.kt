package com.onlinestation.utils

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

open class DiffUtilCallBack<Data>() :
    DiffUtil.ItemCallback<Data>() {
    override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean {
      return  oldItem == newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean {
        return oldItem == newItem
    }

    /* override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
         oldList[oldItemPosition] == newList[newItemPosition]

     override fun getOldListSize(): Int = oldList.size

     override fun getNewListSize(): Int = newList.size

     override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
         return oldList[oldItemPosition] == newList[newItemPosition]
     }*/
}