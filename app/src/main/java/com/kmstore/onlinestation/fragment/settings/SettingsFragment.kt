package com.kmstore.onlinestation.fragment.settings

import com.kmstore.onlinestation.appbase.FragmentBaseMVVM
import com.kmstore.onlinestation.appbase.utils.viewBinding
import com.kmstore.onlinestation.data.entities.Constants.Companion.defaultUserBalanceCount
import com.kmstore.onlinestation.data.entities.Constants.Companion.defaultUserID
import com.kmstore.onlinestation.databinding.FragmentSettingsBinding
import com.kmstore.onlinestation.fragment.settings.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : FragmentBaseMVVM<SettingsViewModel, FragmentSettingsBinding>() {
    override val binding: FragmentSettingsBinding by viewBinding()
    override val viewModel: SettingsViewModel by viewModel()

    override fun onView() {

    }

    override fun onViewClick() {
        binding.addCoinContainer.setOnClickListener {
            viewModel.updateBalance(defaultUserID, defaultUserBalanceCount)
        }
    }

    override fun onEach() {
        onEach(viewModel.balanceCountDataLD) {
            binding.userBalance.text = it?.balance.toString()
        }
    }


}
