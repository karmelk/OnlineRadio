package com.nextidea.onlinestation.fragment.settings

import com.nextidea.onlinestation.appbase.FragmentBaseMVVM
import com.nextidea.onlinestation.appbase.utils.viewBinding
import com.nextidea.onlinestation.data.entities.Constants.Companion.defaultUserBalanceCount
import com.nextidea.onlinestation.data.entities.Constants.Companion.defaultUserID
import com.nextidea.onlinestation.databinding.FragmentSettingsBinding
import com.nextidea.onlinestation.fragment.settings.viewmodel.SettingsViewModel
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
