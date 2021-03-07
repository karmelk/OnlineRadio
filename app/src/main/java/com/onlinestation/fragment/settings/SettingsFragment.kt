package com.onlinestation.fragment.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.kmworks.appbase.FragmentBaseMVVM
import com.kmworks.appbase.utils.Constants
import com.kmworks.appbase.utils.viewBinding
import com.onlinestation.activity.MainActivity
import com.onlinestation.databinding.FragmentFavoriteBinding
import com.onlinestation.databinding.FragmentSettingsBinding
import com.onlinestation.fragment.favorite.FavoriteListStationAdapter
import com.onlinestation.fragment.favorite.viewmodel.FavoriteViewModel
import com.onlinestation.fragment.settings.viewmodel.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : FragmentBaseMVVM<SettingsViewModel, FragmentSettingsBinding>() {
    override val binding: FragmentSettingsBinding by viewBinding()
    override val viewModel: SettingsViewModel by viewModel()

    override fun onView() {

    }

    override fun onViewClick() {
        binding.addCoinContainer.setOnClickListener {
            viewModel.updateBalance(Constants.defaultUserID, Constants.defaultUserBalanceCount)
        }
    }
    override fun observes() {
        observe(viewModel.balanceCountDataLD) {
            binding.userBalance.text = it?.balance.toString()
        }
    }

    override fun navigateUp() {
        navigateBackStack()
    }


}
