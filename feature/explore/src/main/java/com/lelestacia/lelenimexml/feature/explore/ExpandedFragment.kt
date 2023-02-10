package com.lelestacia.lelenimexml.feature.explore

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.paging.PagingData
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lelestacia.lelenimexml.core.model.anime.Anime
import com.lelestacia.lelenimexml.feature.common.adapter.FooterLoadStateAdapter
import com.lelestacia.lelenimexml.feature.common.adapter.ListAnimePagingAdapter
import com.lelestacia.lelenimexml.feature.explore.databinding.FragmentExpandedBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class ExpandedFragment : Fragment(R.layout.fragment_expanded) {

    private val binding: FragmentExpandedBinding by viewBinding()
    private val viewModel: ExpandedViewModel by activityViewModels()
    private val args: ExpandedFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAdapter()

        val listAnimeAdapter = ListAnimePagingAdapter { anime: Anime ->
            Timber.d("Selected anime is ${anime.title}")
            viewLifecycleOwner.lifecycleScope.launch {
                viewModel.insertOrUpdateAnimeToHistory(anime = anime).join()
                val destination = ExpandedFragmentDirections.expandedToDetail(malID = anime.malID)
                findNavController().navigate(destination)
            }
        }
        val mLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        binding.root.apply {
            adapter = listAnimeAdapter.withLoadStateFooter(FooterLoadStateAdapter {
                listAnimeAdapter.retry()
            })
            layoutManager = mLayoutManager
            addItemDecoration(DividerItemDecoration(context, mLayoutManager.orientation))
        }

        when (args.title) {
            getString(R.string.top_anime) -> setTopAnime(adapter = listAnimeAdapter)
            getString(R.string.airing_anime) -> setAiringAnime(adapter = listAnimeAdapter)
            getString(R.string.upcoming_anime) -> setUpcomingAnime()
        }

        /*
         *   1. Fix fragment didn't persist the previous position
         *   2. Implement Trailer on list
         *   3. Fix the load state adapter and make some improvements
         *   4. Wrap everything
         *   5. Refactor everything
         *   6. Make some documentation
         */

    }

    private fun setAdapter() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {

    }

    private fun setTopAnime(adapter: ListAnimePagingAdapter) = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        viewModel.upcomingAnime.collect { anime: PagingData<Anime> ->
            adapter.submitData(
                lifecycle = viewLifecycleOwner.lifecycle,
                pagingData = anime
            )
        }
    }

    private fun setAiringAnime(adapter: ListAnimePagingAdapter) = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        viewModel.airingAnime.collect { anime: PagingData<Anime> ->
            adapter.submitData(
                lifecycle = viewLifecycleOwner.lifecycle,
                pagingData = anime
            )
        }
    }

    private fun setUpcomingAnime() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {

    }
}