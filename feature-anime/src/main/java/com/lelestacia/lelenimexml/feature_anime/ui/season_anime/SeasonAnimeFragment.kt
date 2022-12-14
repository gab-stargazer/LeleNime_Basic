package com.lelestacia.lelenimexml.feature_anime.ui.season_anime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lelestacia.lelenimexml.feature_anime.databinding.FragmentSeasonAnimeBinding
import com.lelestacia.lelenimexml.feature_anime.ui.adapter.AnimePagingAdapter
import com.lelestacia.lelenimexml.feature_anime.ui.adapter.FooterLoadStateAdapter
import com.lelestacia.lelenimexml.feature_anime.ui.adapter.HeaderLoadStateAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeasonAnimeFragment : Fragment() {

    private val viewModel by viewModels<SeasonAnimeViewModel>()
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
        val pagingAdapter = AnimePagingAdapter { anime ->
            val animeEntity = anime.toAnimeEntity()
            viewModel.insertOrUpdateNewAnimeToHistory(animeEntity)
            val action = SeasonAnimeFragmentDirections.airingToDetail(anime)
            findNavController().navigate(action)
        }
        binding.rvSeasonAnime.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pagingAdapter.withLoadStateHeaderAndFooter(
                header = HeaderLoadStateAdapter {
                    pagingAdapter.refresh()
                },
                footer = FooterLoadStateAdapter {
                    pagingAdapter.retry()
                }
            )
            setHasFixedSize(true)
        }
        viewModel.airingAnime
            .observe(viewLifecycleOwner) { anime ->
                pagingAdapter.submitData(lifecycle, anime)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}