package com.lelestacia.lelenimexml.feature.explore

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.lelestacia.lelenimexml.feature.common.adapter.recommendation.RecommendationErrorAdapter
import com.lelestacia.lelenimexml.feature.common.adapter.recommendation.RecommendationItemPagingAdapter
import com.lelestacia.lelenimexml.feature.common.adapter.recommendation.RecommendationPlaceholderAdapter
import com.lelestacia.lelenimexml.feature.common.adapter.util.HorizontalErrorAdapter
import com.lelestacia.lelenimexml.feature.common.adapter.util.HorizontalLoadStateAdapter
import com.lelestacia.lelenimexml.feature.explore.adapter.HorizontalMangaPagingAdapter
import com.lelestacia.lelenimexml.feature.explore.databinding.FragmentDashboardMangaBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import timber.log.Timber

@AndroidEntryPoint
class DashboardMangaFragment : Fragment(R.layout.fragment_dashboard_manga) {

    private val binding: FragmentDashboardMangaBinding by viewBinding()
    private val viewModel: DashboardMangaViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupTopManga()
        listenIntoTopMangaProgress()

        setupRecommendationManga()
        listenIntoRecommendationMangaProgress()
        listenIntoRecommendationMangaPageNumber()
    }

    private val topMangaAdapter: HorizontalMangaPagingAdapter =
        HorizontalMangaPagingAdapter { manga ->

        }

    private fun setupTopManga() =
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            binding.cvHeaderTopManga.setOnClickListener {
                //TODO: Implement Expanded Dashboard for Manga
                Snackbar.make(
                    binding.root,
                    "Expanded Version is not yet implemented",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

            binding.rvTopManga.apply {
                layoutManager =
                    object : LinearLayoutManager(context, RecyclerView.HORIZONTAL, false) {
                        override fun canScrollHorizontally(): Boolean = false
                    }
                setHasFixedSize(true)
            }

            viewModel.topManga.collectLatest { topManga ->
                topMangaAdapter.submitData(
                    lifecycle = viewLifecycleOwner.lifecycle,
                    pagingData = topManga
                )
            }
        }

    private fun listenIntoTopMangaProgress() =
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            val placeHolderAdapter = RecommendationPlaceholderAdapter()

            topMangaAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .collectLatest { loadState ->
                    when (loadState.refresh) {
                        is LoadState.NotLoading -> {
                            Timber.d("Current state is Not Loading")
                            TransitionManager.beginDelayedTransition(binding.shimmerTopManga)
                            if (binding.shimmerTopManga.isShimmerVisible) binding.shimmerTopManga.hideShimmer()
                            binding.rvTopManga.apply {
                                adapter = topMangaAdapter.withLoadStateFooter(
                                    HorizontalLoadStateAdapter(onRetryClicked = topMangaAdapter::retry)
                                )
                                layoutManager =
                                    LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                            }
                        }
                        LoadState.Loading -> {
                            Timber.d("Current state is Loading")
                            if (!binding.shimmerTopManga.isShimmerVisible) binding.shimmerTopManga.showShimmer(
                                true
                            )
                            if (binding.rvTopManga.adapter != placeHolderAdapter) {
                                TransitionManager.beginDelayedTransition(binding.rvTopManga)
                                binding.rvTopManga.adapter = placeHolderAdapter
                            }
                        }
                        is LoadState.Error -> {
                            Timber.d("Current state is Error")
                            if (binding.rvTopManga.adapter == placeHolderAdapter) {
                                if (binding.shimmerTopManga.isShimmerVisible) binding.shimmerTopManga.hideShimmer()
                                TransitionManager.beginDelayedTransition(binding.root)
                                binding.rvTopManga.adapter = HorizontalErrorAdapter(
                                    message = (loadState.refresh as LoadState.Error).error.message
                                        ?: "Unknown",
                                    onRetryClicked = topMangaAdapter::retry
                                )
                            }
                        }
                    }
                }
        }

    private val mangaRecommendationAdapter = RecommendationItemPagingAdapter(
        onItemClicked = { recommendation ->
            //TODO: Implement Bottom Fragment View for Recommendation Item
        },
        onNextButtonClicked = {
            val currentPosition =
                (binding.rvRecommendationManga.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
            binding.rvRecommendationManga.smoothScrollToPosition(currentPosition + 1)
        }
    )

    private fun setupRecommendationManga() =
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            val mLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            val snapHelper = LinearSnapHelper()
            binding.rvRecommendationManga.apply {
                adapter = mangaRecommendationAdapter
                layoutManager = mLayoutManager
                addItemDecoration(DividerItemDecoration(context, mLayoutManager.orientation))
                snapHelper.attachToRecyclerView(this)
            }

            viewModel.mangaRecommendation.collectLatest { recommendationAnime ->
                mangaRecommendationAdapter.submitData(
                    lifecycle = viewLifecycleOwner.lifecycle,
                    pagingData = recommendationAnime
                )
            }
        }

    private fun listenIntoRecommendationMangaProgress() =
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            val placeHolderRecommendationAdapter = RecommendationPlaceholderAdapter()

            mangaRecommendationAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .collectLatest { loadState ->
                    when (loadState.refresh) {
                        is LoadState.NotLoading -> {
                            Timber.d("Current state is Not Loading")
                            TransitionManager.beginDelayedTransition(
                                binding.root,
                                AutoTransition()
                            )
                            binding.rvRecommendationManga.apply {
                                adapter = mangaRecommendationAdapter
                                layoutManager =
                                    LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                            }
                            binding.tvRecommendationPageNumber.apply {
                                text = getString(
                                    R.string.recommendation_page_number,
                                    1,
                                    mangaRecommendationAdapter.itemCount
                                )
                                visibility = View.VISIBLE
                            }
                        }
                        LoadState.Loading -> {
                            Timber.d("Current state is Loading")
                            if (binding.rvRecommendationManga.adapter != placeHolderRecommendationAdapter) {
                                TransitionManager.beginDelayedTransition(binding.root)
                                binding.rvRecommendationManga.adapter =
                                    placeHolderRecommendationAdapter
                            }
                        }
                        is LoadState.Error -> {
                            Timber.d("Current state is Error")
                            if (binding.rvRecommendationManga.adapter == placeHolderRecommendationAdapter) {
                                TransitionManager.beginDelayedTransition(
                                    binding.root,
                                    AutoTransition()
                                )
                                binding.rvRecommendationManga.adapter = RecommendationErrorAdapter(
                                    message = (loadState.refresh as LoadState.Error).error.message
                                        ?: "Unknown",
                                    onRetry = mangaRecommendationAdapter::retry
                                )
                            }
                        }
                    }
                }
        }

    private fun listenIntoRecommendationMangaPageNumber() =
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            binding.rvRecommendationManga.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val currentPosition =
                            (recyclerView.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                        binding.tvRecommendationPageNumber.text =
                            getString(
                                R.string.recommendation_page_number,
                                currentPosition.plus(1),
                                mangaRecommendationAdapter.itemCount
                            )
                    }
                }
            })
        }
}