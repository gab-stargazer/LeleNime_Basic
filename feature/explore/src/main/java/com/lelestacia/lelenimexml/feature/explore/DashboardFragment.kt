package com.lelestacia.lelenimexml.feature.explore

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.lelestacia.lelenimexml.feature.common.R.array.dashboard_title
import com.lelestacia.lelenimexml.feature.explore.adapter.DashboardPagerAdapter
import com.lelestacia.lelenimexml.feature.explore.databinding.FragmentDashboardBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardFragment : Fragment(R.layout.fragment_dashboard) {

    private val binding: FragmentDashboardBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val arrTitle: Array<String> = resources.getStringArray(dashboard_title)
        val pagerAdapter = DashboardPagerAdapter(activity = requireActivity() as AppCompatActivity)
        val viewPager = binding.vpDashboard
        viewPager.apply {
            isUserInputEnabled = false
            adapter = pagerAdapter
        }
        TabLayoutMediator(binding.tabLayoutDashboard, viewPager, false, true) { tab, position ->
            tab.text = arrTitle[position]
        }.attach()
    }
}