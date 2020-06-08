package com.onlinestation.fragment.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer

import com.onlinestation.R
import com.onlinestation.databinding.FragmentSettingsBinding
import com.onlinestation.entities.responcemodels.OwnerUserBalance
import com.onlinestation.fragment.settings.viewmodel.SettingsViewModel
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {
    private val settingsViewModel: SettingsViewModel by viewModel()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding:FragmentSettingsBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_settings, container, false)
        binding.lifecycleOwner = this
        binding.viewModel = settingsViewModel
        return binding.root
    }
}
