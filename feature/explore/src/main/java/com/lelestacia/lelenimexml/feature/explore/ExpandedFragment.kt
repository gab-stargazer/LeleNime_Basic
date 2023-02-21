package com.lelestacia.lelenimexml.feature.explore

import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lelestacia.lelenimexml.core.common.util.Constant.UNKNOWN
import com.lelestacia.lelenimexml.feature.common.adapter.anime.ListAnimePagingAdapter
import com.lelestacia.lelenimexml.feature.common.adapter.manga.ItemListMangaPagingAdapter
import com.lelestacia.lelenimexml.feature.common.adapter.util.ItemListPlaceholderAdapter
import com.lelestacia.lelenimexml.feature.common.adapter.util.ItemListScreenErrorAdapter
import com.lelestacia.lelenimexml.feature.explore.adapter.util.ExpandedFooterLoadStateAdapter
import com.lelestacia.lelenimexml.feature.explore.databinding.FragmentExpandedBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import timber.log.Timber

@AndroidEntryPoint
class ExpandedFragment : Fragment(R.layout.fragment_expanded) {

    private val binding: FragmentExpandedBinding by viewBinding()
    private val viewModel: ExpandedViewModel by viewModels()
    private val args: ExpandedFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupExpandedRecyclerview()
        /*
         *   1. Fix fragment didn't persist the previous position
         *   2. Implement Trailer on list
         *   3. Fix the load state adapter and make some improvements
         *   4. Wrap everything
         *   5. Refactor everything
         *   6. Make some documentation
         */
    }

    private val placeHolderAdapter: ItemListPlaceholderAdapter = ItemListPlaceholderAdapter()
    private val animeAdapter = ListAnimePagingAdapter { anime ->
        //TODO: DO SOMETHING
    }

    private val mangaAdapter = ItemListMangaPagingAdapter { manga ->
        //TODO: DO SOMETHING
    }

    private fun setupExpandedRecyclerview() =
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            val mLayoutManager =
                object : LinearLayoutManager(
                    requireContext(),
                    RecyclerView.VERTICAL,
                    false
                ) {
                    override fun canScrollHorizontally(): Boolean = false
                }
            binding.rvListItem.apply {
                adapter = placeHolderAdapter
                layoutManager = mLayoutManager
                setHasFixedSize(true)
                addItemDecoration(
                    DividerItemDecoration(
                        requireContext(),
                        mLayoutManager.orientation
                    )
                )
            }

            when (args.title) {
                getString(R.string.trending_anime) -> {
                    listenIntoAnimeProgress()
                    viewModel.trendingAnime.collectLatest { trendingAnime ->
                        animeAdapter.submitData(
                            lifecycle = viewLifecycleOwner.lifecycle,
                            pagingData = trendingAnime
                        )
                    }
                }

                getString(R.string.airing_anime) -> {
                    listenIntoAnimeProgress()
                    viewModel.airingAnime.collectLatest { airingAnime ->
                        animeAdapter.submitData(
                            lifecycle = viewLifecycleOwner.lifecycle,
                            pagingData = airingAnime
                        )
                    }
                }

                getString(R.string.upcoming_anime) -> {
                    listenIntoAnimeProgress()
                    viewModel.upcomingAnime.collectLatest { upcomingAnime ->
                        animeAdapter.submitData(
                            lifecycle = viewLifecycleOwner.lifecycle,
                            pagingData = upcomingAnime
                        )
                    }
                }

                else -> {
                    listenIntoMangaProgress()
                    viewModel.trendingManga.collectLatest { trendingManga ->
                        mangaAdapter.submitData(
                            lifecycle = viewLifecycleOwner.lifecycle,
                            pagingData = trendingManga
                        )
                    }
                }
            }
        }

    private fun listenIntoAnimeProgress() =
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            animeAdapter
                .loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .collectLatest { loadState ->
                    when (loadState.refresh) {
                        is LoadState.NotLoading -> {
                            TransitionManager.beginDelayedTransition(binding.rvListItem)
                            binding.root.hideShimmer()
                            binding.rvListItem.apply {
                                adapter =
                                    animeAdapter.withLoadStateFooter(
                                        footer = ExpandedFooterLoadStateAdapter {
                                            mangaAdapter.retry()
                                        })
                                layoutManager = LinearLayoutManager(
                                    requireContext(),
                                    RecyclerView.VERTICAL,
                                    false
                                )
                            }
                        }

                        LoadState.Loading -> {
                            TransitionManager.beginDelayedTransition(binding.rvListItem)
                            if (binding.rvListItem.adapter != placeHolderAdapter) {
                                binding.rvListItem.adapter = placeHolderAdapter
                                binding.root.showShimmer(true)
                            }
                        }

                        is LoadState.Error -> {
                            TransitionManager.beginDelayedTransition(binding.rvListItem)
                            binding.root.hideShimmer()
                            if (binding.rvListItem.adapter == placeHolderAdapter) {
                                binding.rvListItem.adapter =
                                    ItemListScreenErrorAdapter(
                                        errorMessage =
                                        (loadState.refresh as LoadState.Error).error.message
                                            ?: UNKNOWN,
                                        onRetry = {
                                            animeAdapter.retry()
                                        }
                                    )
                            }
                        }
                    }
                }
        }

    private fun listenIntoMangaProgress() =
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            mangaAdapter
                .loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .collectLatest { loadState ->
                    when (loadState.refresh) {
                        is LoadState.NotLoading -> {
                            TransitionManager.beginDelayedTransition(binding.rvListItem)
                            binding.root.hideShimmer()
                            binding.rvListItem.apply {
                                adapter =
                                    mangaAdapter.withLoadStateFooter(
                                        footer = ExpandedFooterLoadStateAdapter {
                                            mangaAdapter.retry()
                                        })
                                layoutManager = LinearLayoutManager(
                                    requireContext(),
                                    RecyclerView.VERTICAL,
                                    false
                                )
                            }
                        }

                        LoadState.Loading -> {
                            TransitionManager.beginDelayedTransition(binding.rvListItem)
                            if (binding.rvListItem.adapter != placeHolderAdapter) {
                                binding.rvListItem.adapter = placeHolderAdapter
                                binding.root.showShimmer(true)
                            }
                        }

                        is LoadState.Error -> {
                            TransitionManager.beginDelayedTransition(binding.rvListItem)
                            binding.root.hideShimmer()
                            if (binding.rvListItem.adapter == placeHolderAdapter) {
                                binding.rvListItem.adapter =
                                    ItemListScreenErrorAdapter(
                                        errorMessage =
                                        (loadState.refresh as LoadState.Error).error.message
                                            ?: UNKNOWN,
                                        onRetry = {
                                            mangaAdapter.retry()
                                        }
                                    )
                            }
                        }
                    }
                }
        }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.tag("Fragment Logger").w("Fragment Dashboard Expanded View was Destroyed")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.tag("Fragment Logger").w("Fragment Expanded was Destroyed")
    }
}