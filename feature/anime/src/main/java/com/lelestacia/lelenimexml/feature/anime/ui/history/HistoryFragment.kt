package com.lelestacia.lelenimexml.feature.anime.ui.history

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.lelestacia.lelenimexml.feature.anime.R
import com.lelestacia.lelenimexml.feature.anime.databinding.FragmentHistoryBinding
import com.lelestacia.lelenimexml.feature.anime.ui.adapter.ListAnimePagingAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HistoryFragment : Fragment(R.layout.fragment_history) {

    private val binding: FragmentHistoryBinding by viewBinding()
    private val viewModel: HistoryViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val historyAnimeAdapter = ListAnimePagingAdapter { anime ->
            viewModel.insertOrUpdateAnime(anime)
            val action = HistoryFragmentDirections.historyToDetail(anime.malID)
            view.findNavController().navigate(action)
        }

        val myLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.rvAnime.apply {
            layoutManager = myLayoutManager
            adapter = historyAnimeAdapter
            addItemDecoration(DividerItemDecoration(context, myLayoutManager.orientation))
            setHasFixedSize(true)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.recentlyViewedAnime.collect { recentlyViewedAnime ->
                historyAnimeAdapter.submitData(lifecycle, recentlyViewedAnime)
            }
        }
    }
}