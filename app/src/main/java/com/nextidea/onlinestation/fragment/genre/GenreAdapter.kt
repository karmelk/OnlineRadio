package com.nextidea.onlinestation.fragment.genre

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.nextidea.onlinestation.R
import com.nextidea.onlinestation.appbase.adapter.BaseAdapter
import com.nextidea.onlinestation.appbase.adapter.BasePagingAdapter
import com.nextidea.onlinestation.appbase.adapter.BaseViewHolder
import com.nextidea.onlinestation.databinding.ItemGenreBinding
import com.nextidea.onlinestation.data.entities.request.GenderItem

class GenreAdapter(val goToStationPage:(id:Int)->Unit) :
    BasePagingAdapter<ViewBinding, GenderItem, BaseViewHolder<GenderItem, ViewBinding>>() {

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
                    .load(item.isoCode)
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
            goToStationPage.invoke(item.stationcount)
        }
    }

}


