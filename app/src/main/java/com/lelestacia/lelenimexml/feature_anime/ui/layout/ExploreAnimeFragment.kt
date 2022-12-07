package com.lelestacia.lelenimexml.feature_anime.ui.layout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.lelestacia.lelenimexml.databinding.FragmentExploreAnimeBinding
import com.lelestacia.lelenimexml.feature_anime.ui.adapter.AnimeRowPagingAdapter
import com.lelestacia.lelenimexml.feature_anime.ui.adapter.FooterLoadStateAdapter
import com.lelestacia.lelenimexml.feature_anime.ui.adapter.HeaderLoadStateAdapter
import com.lelestacia.lelenimexml.feature_anime.ui.viewmodel.AnimeViewModel
import kotlinx.coroutines.Dispatchers

class ExploreAnimeFragment : Fragment() {

    private val viewModel by activityViewModels<AnimeViewModel>()
    private var _binding: FragmentExploreAnimeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreAnimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pagingAdapter = AnimeRowPagingAdapter()
        binding.rvAnimeResult.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pagingAdapter.withLoadStateHeaderAndFooter(
                header = HeaderLoadStateAdapter {
                    pagingAdapter.retry()
                },
                footer = FooterLoadStateAdapter {
                    pagingAdapter.retry()
                }
            )
            setHasFixedSize(true)
        }
        viewModel.searchAnimeByTitle()
            .asLiveData(Dispatchers.Main)
            .observe(viewLifecycleOwner) {
                pagingAdapter.submitData(lifecycle, it)
            }
        binding.btnSearch.setOnClickListener {
            viewModel.searchNewQuery(binding.edtAnimeTitle.text.toString())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}