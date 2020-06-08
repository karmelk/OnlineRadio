package com.onlinestation.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.content.ContextCompat
import com.onlinestation.R
import com.onlinestation.entities.localmodels.GenderItem
import kotlinx.android.synthetic.main.item_message.view.*

class PopUpGenderAdapter(
    private val context: Activity,
    private val genderList: MutableList<GenderItem>,
    private val genderNameCB: (genderName: String?) -> Unit
) : ArrayAdapter<GenderItem>(context, R.layout.item_message, genderList) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        val rowView = context.layoutInflater.inflate(R.layout.item_message, null, true)
        val item = genderList[position]
        val message = rowView.findViewById(R.id.message) as AppCompatTextView
        item.apply {
            message.text = genderName
            message.background = if (selectedItem) {
                ContextCompat.getDrawable(
                    context,
                    R.drawable.border_message_item_selected
                )
            } else {
                ContextCompat.getDrawable(
                    context,
                    R.drawable.border_message_item
                )
            }
        }
        message.setOnClickListener {
            if (item.genderName == "All") {
                genderNameCB.invoke(null)
            } else {
                genderNameCB.invoke(item.genderName)
            }

        }
        return rowView
    }
}