package com.lelestacia.lelenimexml.feature_anime.ui.layout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.lelestacia.lelenimexml.databinding.FragmentSeasonAnimeBinding
import com.lelestacia.lelenimexml.feature_anime.ui.adapter.AnimeRowPagingAdapter
import com.lelestacia.lelenimexml.feature_anime.ui.viewmodel.AnimeViewModel


class SeasonAnimeFragment : Fragment() {

    private val viewModel by activityViewModels<AnimeViewModel>()
    private var _binding: FragmentSeasonAnimeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeasonAnimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = AnimeRowPagingAdapter()
        lifecycleScope.launchWhenCreated {
            viewModel.seasonAnimePagingData().collect {
                adapter.submitData(lifecycle, it)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}