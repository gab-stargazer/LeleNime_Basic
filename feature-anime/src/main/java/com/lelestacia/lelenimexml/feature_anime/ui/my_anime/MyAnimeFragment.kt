package com.lelestacia.lelenimexml.feature_anime.ui.my_anime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lelestacia.lelenimexml.feature_anime.databinding.FragmentMyListAnimeBinding
import com.lelestacia.lelenimexml.feature_anime.ui.adapter.HistoryAnimeAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyAnimeFragment : Fragment() {

    private val viewModel by viewModels<MyAnimeViewModel>()
    private var _binding: FragmentMyListAnimeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMyListAnimeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = HistoryAnimeAdapter { anime ->
            val animeEntity = anime.toAnimeEntity()
            viewModel.insertOrUpdateNewAnimeToHistory(animeEntity)
            val action = MyAnimeFragmentDirections.historyToDetail(anime)
            view.findNavController().navigate(action)
        }
        binding.apply {
            rvAnimeHistory.adapter = adapter
            rvAnimeHistory.layoutManager = LinearLayoutManager(requireContext())
        }
        viewModel.listOfAnime.observe(viewLifecycleOwner) { animeHistories ->
            adapter.submitList(animeHistories)
        }
    }
}