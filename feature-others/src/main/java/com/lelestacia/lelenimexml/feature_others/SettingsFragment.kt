package com.lelestacia.lelenimexml.feature_others

import android.content.Context
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import com.lelestacia.lelenimexml.core.utility.Constant.IS_SFW
import com.lelestacia.lelenimexml.core.utility.Constant.USER_PREF
import com.lelestacia.lelenimexml.feature_others.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private val binding: FragmentSettingsBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val sharedPreferences =
            requireContext().getSharedPreferences(USER_PREF, Context.MODE_PRIVATE)
        binding.switchMode.isChecked = sharedPreferences.getBoolean(IS_SFW, true)
        binding.switchMode.setOnCheckedChangeListener { buttonView, isChecked ->
            sharedPreferences.edit().putBoolean(IS_SFW, isChecked).apply()
            buttonView.isChecked = isChecked
        }
    }
}