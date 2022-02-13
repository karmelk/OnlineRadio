package com.kmstore.onlinestation.fragment.genre

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kmstore.onlinestation.R
import com.kmstore.onlinestation.appbase.adapter.BaseAdapter
import com.kmstore.onlinestation.appbase.adapter.BaseViewHolder
import com.kmstore.onlinestation.databinding.ItemGenreBinding
import com.kmstore.onlinestation.data.entities.request.GenderItem

class GenreAdapter(val goToStationPage:(id:Long)->Unit) :
    BaseAdapter<ViewBinding, GenderItem, BaseViewHolder<GenderItem, ViewBinding>>(
        ItemsDiffCallback { oldItem, newItem -> oldItem.id == newItem.id }
    ) {

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

        override fun bind(item: GenderItem, context: Context) {
            with(binding) {
                genreName.text = item.name
                Glide.with(context)
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

}

