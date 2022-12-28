package com.lelestacia.lelenimexml.feature.others

import android.content.Context
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatDelegate.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.lelestacia.lelenimexml.core.common.Constant.IS_DARK_MODE
import com.lelestacia.lelenimexml.core.common.Constant.IS_SFW
import com.lelestacia.lelenimexml.core.common.Constant.USER_PREF
import com.lelestacia.lelenimexml.feature.others.databinding.FragmentSettingsBinding

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
        val array: Array<String> = resources.getStringArray(R.array.system_mode)
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.item_tv,
            array
        )

        binding.tvSystemTheme.setText(array[sharedPreferences.getInt(IS_DARK_MODE, 0)])
        binding.tvSystemTheme.setAdapter(adapter)

        binding.tvSystemTheme.addTextChangedListener {
            binding.tvSystemTheme.setAdapter(adapter)
            when(it.toString()) {
                array[0] -> {
                    sharedPreferences.edit().putInt(IS_DARK_MODE, 0).apply()
                }
                array[1] ->  {
                    setDefaultNightMode(MODE_NIGHT_YES)
                    sharedPreferences.edit().putInt(IS_DARK_MODE, 1).apply()
                }
                array[2] -> {
                    setDefaultNightMode(MODE_NIGHT_NO)
                    sharedPreferences.edit().putInt(IS_DARK_MODE, 2).apply()
                }
            }
        }
    }
}