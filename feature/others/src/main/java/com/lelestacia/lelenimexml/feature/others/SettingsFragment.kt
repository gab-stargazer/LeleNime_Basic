package com.lelestacia.lelenimexml.feature.others

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.lelestacia.lelenimexml.feature.others.databinding.FragmentSettingsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding: FragmentSettingsBinding by viewBinding()
    private val viewModel: SettingsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.switchMode.isChecked = viewModel.isSafeMode()
        binding.switchMode.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.changeSafeMode(isChecked)
            buttonView.isChecked = isChecked
        }
    }
}
