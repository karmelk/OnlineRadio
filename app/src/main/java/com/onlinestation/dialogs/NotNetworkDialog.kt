package com.onlinestation.dialogs

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.onlinestation.R
import com.onlinestation.appbase.utils.viewBinding
import com.onlinestation.databinding.DialogNotNetworkBinding

class NotNetworkDialog : DialogFragment() {
    companion object {
        const val DIALOG_TAG = "EnableBluetooth"
        fun newInstance(): NotNetworkDialog {
            return NotNetworkDialog()
        }
    }

    private val binding: DialogNotNetworkBinding by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = binding.root


    override fun onResume() {
        super.onResume()
        dialog?.window?.run {
            setLayout(resources.getDimensionPixelSize(R.dimen.dp_335),
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        }
    }
}