package com.onlinestation.fragment.genre

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.onlinestation.appbase.adapter.BaseAdapter
import com.onlinestation.appbase.adapter.BaseViewHolder
import com.onlinestation.R
import com.onlinestation.databinding.ItemGenreBinding
import com.onlinestation.data.entities.request.GenderItem

class GenreAdapter(val goToStationPage:(id:Long)->Unit) :
    BaseAdapter<ViewBinding, GenderItem, BaseViewHolder<GenderItem, ViewBinding>>(DiffCallback()) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseViewHolder<GenderItem, ViewBinding> = MyViewHolder(
        ItemGenreBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    private inner class MyViewHolder(private val binding: ItemGenreBinding) :
        BaseViewHolder<GenderItem, ViewBinding>(binding) {

        override fun bind(item: GenderItem) {
            with(binding) {
                genreName.text = item.name
                Glide.with(binding.root.context)
                    .load(item.img)
                    .centerCrop()
                    .circleCrop()
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.ic_default_radio)
                            .error(R.drawable.ic_default_radio)
                    )
                    .into(genderLogo)
            }

        }
        override fun onItemClick(item: GenderItem) {
            goToStationPage.invoke(item.id)
        }
    }

    private  class DiffCallback : DiffUtil.ItemCallback<GenderItem>() {
        override fun areItemsTheSame(oldItem: GenderItem, newItem: GenderItem): Boolean =
            oldItem.id == newItem.id

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: GenderItem, newItem: GenderItem): Boolean =
            oldItem == newItem

        override fun getChangePayload(oldItem: GenderItem, newItem: GenderItem): Any? {
            return super.getChangePayload(oldItem, newItem)
        }
    }
}


