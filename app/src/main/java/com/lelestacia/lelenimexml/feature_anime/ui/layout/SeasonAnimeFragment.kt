package com.lelestacia.lelenimexml.feature_anime.ui.layout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import com.lelestacia.lelenimexml.databinding.FragmentSeasonAnimeBinding
import com.lelestacia.lelenimexml.feature_anime.domain.model.AnimeCard
import com.lelestacia.lelenimexml.feature_anime.ui.adapter.AnimeRowPagingAdapter
import com.lelestacia.lelenimexml.feature_anime.ui.adapter.FooterLoadStateAdapter
import com.lelestacia.lelenimexml.feature_anime.ui.adapter.HeaderLoadStateAdapter
import com.lelestacia.lelenimexml.feature_anime.ui.viewmodel.AnimeViewModel
import kotlinx.coroutines.Dispatchers


class SeasonAnimeFragment : Fragment() {

    private lateinit var pagingAdapter: AnimeRowPagingAdapter
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
        pagingAdapter = AnimeRowPagingAdapter()
        binding.rvSeasonAnime.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pagingAdapter.withLoadStateHeaderAndFooter(
                header = HeaderLoadStateAdapter {
                    pagingAdapter::refresh
                },
                footer = FooterLoadStateAdapter {
                    pagingAdapter::retry
                }
            )
            setHasFixedSize(true)
        }
        viewModel.seasonAnimePagingData()
            .asLiveData(Dispatchers.Main)
            .observe(viewLifecycleOwner) {
                pagingAdapter.submitData(lifecycle, it as PagingData<AnimeCard>)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}