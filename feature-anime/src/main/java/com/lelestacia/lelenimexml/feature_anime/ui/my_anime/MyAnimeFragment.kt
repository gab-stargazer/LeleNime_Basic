package com.lelestacia.lelenimexml.feature_anime.ui.my_anime

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lelestacia.lelenimexml.feature_anime.R
import com.lelestacia.lelenimexml.feature_anime.databinding.FragmentMyListAnimeBinding
import com.lelestacia.lelenimexml.feature_anime.ui.adapter.ListAnimePagingAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyAnimeFragment : Fragment(R.layout.fragment_my_list_anime) {

    private val viewModel by viewModels<MyAnimeViewModel>()
    private val binding: FragmentMyListAnimeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ListAnimePagingAdapter { anime ->
            viewModel.insertNewOrUpdateLastViewed(anime)
            val action = MyAnimeFragmentDirections.historyToDetail(anime)
            view.findNavController().navigate(action)
        }
        binding.apply {
            rvAnimeHistory.adapter = adapter
            rvAnimeHistory.layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.listOfAnime.observe(viewLifecycleOwner) { animeHistories ->
            adapter.submitData(lifecycle, animeHistories)
        }
    }
}