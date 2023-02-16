package com.lelestacia.lelenimexml.feature.explore.adapter

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.lelestacia.lelenimexml.feature.explore.DashboardAnimeFragment
import com.lelestacia.lelenimexml.feature.explore.DashboardMangaFragment

class DashboardPagerAdapter(activity: AppCompatActivity) : FragmentStateAdapter(activity) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DashboardAnimeFragment()
            else -> DashboardMangaFragment()
        }
    }
}