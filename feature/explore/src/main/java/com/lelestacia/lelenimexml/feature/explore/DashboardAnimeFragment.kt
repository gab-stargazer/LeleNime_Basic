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
import com.lelestacia.lelenimexml.feature.common.adapter.anime.AnimeHorizontalPagingAdapter
import com.lelestacia.lelenimexml.feature.common.adapter.recommendation.RecommendationErrorAdapter
import com.lelestacia.lelenimexml.feature.common.adapter.recommendation.RecommendationItemPagingAdapter
import com.lelestacia.lelenimexml.feature.common.adapter.recommendation.RecommendationPlaceholderAdapter
import com.lelestacia.lelenimexml.feature.common.adapter.util.HorizontalErrorAdapter
import com.lelestacia.lelenimexml.feature.common.adapter.util.HorizontalLoadStateAdapter
import com.lelestacia.lelenimexml.feature.explore.adapter.PlaceHolderHorizontalAdapter
import com.lelestacia.lelenimexml.feature.explore.databinding.FragmentDashboardAnimeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import timber.log.Timber

@AndroidEntryPoint
class DashboardAnimeFragment : Fragment(R.layout.fragment_dashboard_anime) {

    private val viewModel: DashboardAnimeViewModel by viewModels()
    private val binding: FragmentDashboardAnimeBinding by viewBinding()
    private val placeHolderAdapter: PlaceHolderHorizontalAdapter =
        PlaceHolderHorizontalAdapter(placeHolderCount = (1..4).toList())
    private val topAnimeAdapter = AnimeHorizontalPagingAdapter { anime ->
        Snackbar.make(
            binding.root,
            "Selected anime is: ${anime.title}",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private val airingAnimeAdapter = AnimeHorizontalPagingAdapter { anime ->
        Snackbar.make(
            binding.root,
            "Selected anime is: ${anime.title}",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    private val upcomingAnimeAdapter = AnimeHorizontalPagingAdapter { anime ->
        Snackbar.make(
            binding.root,
            "Selected anime is: ${anime.title}",
            Snackbar.LENGTH_SHORT
        ).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTopAnime()
        listenIntoTopAnimeProgress()

        setAiringAnime()
        listenIntoAiringAnimeProgress()

        setUpcomingAnime()
        listenIntoUpcomingAnimeProgress()

        setUpRecommendation()
        listenIntoRecommendationAnimeProgress()
        listenIntoRecommendationAnimePageNumber()
    }


    private fun setTopAnime() =
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            binding.cvHeaderTrendingAnime.setOnClickListener {
                //TODO: Implement Expanded Dashboard for Anime
                Snackbar.make(
                    binding.root,
                    "Expanded Version is not yet implemented",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            binding.rvTrendingAnime.apply {
                layoutManager =
                    object : LinearLayoutManager(context, RecyclerView.HORIZONTAL, false) {
                        override fun canScrollHorizontally(): Boolean = false
                    }
                setHasFixedSize(true)
            }

            viewModel.topAnime.collectLatest { topAnime ->
                topAnimeAdapter.submitData(
                    lifecycle = viewLifecycleOwner.lifecycle,
                    pagingData = topAnime
                )
            }
        }

    private fun listenIntoTopAnimeProgress() =
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            val rvTopAnime = binding.rvTrendingAnime
            val shimmerTopAnime = binding.shimmerTrendingAnime
            topAnimeAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .collectLatest { loadState ->
                    when (loadState.refresh) {
                        is LoadState.NotLoading -> {
                            Timber.d("Current state is Not Loading")
                            TransitionManager.beginDelayedTransition(rvTopAnime)
                            if (shimmerTopAnime.isShimmerVisible) shimmerTopAnime.hideShimmer()
                            rvTopAnime.apply {
                                adapter = topAnimeAdapter.withLoadStateFooter(
                                    HorizontalLoadStateAdapter(onRetryClicked = topAnimeAdapter::retry)
                                )
                                layoutManager =
                                    LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                            }
                        }
                        LoadState.Loading -> {
                            Timber.d("Current state is Loading")
                            if (!shimmerTopAnime.isShimmerVisible) shimmerTopAnime.showShimmer(true)
                            if (rvTopAnime.adapter != placeHolderAdapter) {
                                TransitionManager.beginDelayedTransition(binding.root)
                                rvTopAnime.adapter = placeHolderAdapter
                            }
                        }
                        is LoadState.Error -> {
                            Timber.d("Current state is Error")
                            if (shimmerTopAnime.isShimmerVisible) shimmerTopAnime.hideShimmer()
                            TransitionManager.beginDelayedTransition(binding.root)
                            if (rvTopAnime.adapter == placeHolderAdapter) {
                                rvTopAnime.adapter = HorizontalErrorAdapter(
                                    message = (loadState.refresh as LoadState.Error).error.message
                                        ?: "Unknown",
                                    onRetryClicked = topAnimeAdapter::retry
                                )
                            }
                        }
                    }
                }
        }

    private fun setAiringAnime() =
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            val rvAiringAnime = binding.rvAiringAnime
            binding.cvHeaderAiringAnime.setOnClickListener {
                //TODO: Implement Expanded Dashboard for Anime
                Snackbar.make(
                    binding.root,
                    "Expanded Version is not yet implemented",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            rvAiringAnime.apply {
                layoutManager =
                    object : LinearLayoutManager(context, RecyclerView.HORIZONTAL, false) {
                        override fun canScrollHorizontally(): Boolean = false
                    }
                setHasFixedSize(true)
            }

            viewModel.airingAnime.collectLatest { airingAnime ->
                airingAnimeAdapter.submitData(
                    lifecycle = viewLifecycleOwner.lifecycle,
                    pagingData = airingAnime
                )
            }
        }

    private fun listenIntoAiringAnimeProgress() =
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            val rvAiringAnime = binding.rvAiringAnime
            val shimmerAiringAnime = binding.shimmerAiringAnime
            airingAnimeAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .collectLatest { loadState ->
                    when (loadState.refresh) {
                        is LoadState.NotLoading -> {
                            Timber.d("Current state is Not Loading")
                            TransitionManager.beginDelayedTransition(binding.rvAiringAnime)
                            if (shimmerAiringAnime.isShimmerVisible) shimmerAiringAnime.hideShimmer()
                            rvAiringAnime.apply {
                                adapter = airingAnimeAdapter.withLoadStateFooter(
                                    HorizontalLoadStateAdapter(onRetryClicked = airingAnimeAdapter::retry)
                                )
                                layoutManager =
                                    LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                            }
                        }
                        LoadState.Loading -> {
                            Timber.d("Current state is Loading")
                            if (!shimmerAiringAnime.isShimmerVisible) shimmerAiringAnime.showShimmer(
                                true
                            )
                            if (rvAiringAnime.adapter != placeHolderAdapter) {
                                TransitionManager.beginDelayedTransition(binding.root)
                                rvAiringAnime.adapter = placeHolderAdapter
                            }
                        }
                        is LoadState.Error -> {
                            Timber.d("Current state is Error")
                            if (rvAiringAnime.adapter == placeHolderAdapter) {
                                if (shimmerAiringAnime.isShimmerVisible) shimmerAiringAnime.hideShimmer()
                                TransitionManager.beginDelayedTransition(binding.root)
                                rvAiringAnime.adapter = HorizontalErrorAdapter(
                                    message = (loadState.refresh as LoadState.Error).error.message
                                        ?: "Unknown",
                                    onRetryClicked = airingAnimeAdapter::retry
                                )
                            }
                        }
                    }
                }
        }

    private fun setUpcomingAnime() =
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            val rvUpcomingAnime = binding.rvUpcomingAnime
            binding.cvHeaderUpcomingAnime.setOnClickListener {
                //TODO: Implement Expanded Dashboard for Anime
                Snackbar.make(
                    binding.root,
                    "Expanded Version is not yet implemented",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
            rvUpcomingAnime.apply {
                layoutManager =
                    object : LinearLayoutManager(context, RecyclerView.HORIZONTAL, false) {
                        override fun canScrollHorizontally(): Boolean = false
                    }
                setHasFixedSize(true)
            }

            viewModel.upcomingAnime.collectLatest { upcomingAnime ->
                upcomingAnimeAdapter.submitData(
                    lifecycle = viewLifecycleOwner.lifecycle,
                    pagingData = upcomingAnime
                )
            }
        }

    private fun listenIntoUpcomingAnimeProgress() =
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            val rvUpcomingAnime = binding.rvUpcomingAnime
            val shimmerUpcomingAnime = binding.shimmerUpcomingAnime
            upcomingAnimeAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .collectLatest { loadState ->
                    when (loadState.refresh) {
                        is LoadState.NotLoading -> {
                            Timber.d("Current state is Not Loading")
                            TransitionManager.beginDelayedTransition(rvUpcomingAnime)
                            if (shimmerUpcomingAnime.isShimmerVisible) shimmerUpcomingAnime.hideShimmer()
                            rvUpcomingAnime.apply {
                                adapter = upcomingAnimeAdapter.withLoadStateFooter(
                                    HorizontalLoadStateAdapter(onRetryClicked = upcomingAnimeAdapter::retry)
                                )
                                layoutManager =
                                    LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                            }
                        }
                        LoadState.Loading -> {
                            Timber.d("Current state is Loading")
                            if (!shimmerUpcomingAnime.isShimmerVisible) shimmerUpcomingAnime.showShimmer(
                                true
                            )
                            if (rvUpcomingAnime.adapter != placeHolderAdapter) {
                                TransitionManager.beginDelayedTransition(binding.root)
                                rvUpcomingAnime.adapter = placeHolderAdapter
                            }
                        }
                        is LoadState.Error -> {
                            Timber.d("Current state is Error")
                            if (rvUpcomingAnime.adapter == placeHolderAdapter) {
                                if (shimmerUpcomingAnime.isShimmerVisible) shimmerUpcomingAnime.hideShimmer()
                                TransitionManager.beginDelayedTransition(binding.root)
                                rvUpcomingAnime.adapter = HorizontalErrorAdapter(
                                    message = (loadState.refresh as LoadState.Error).error.message
                                        ?: "Unknown",
                                    onRetryClicked = upcomingAnimeAdapter::retry
                                )
                            }
                        }
                    }
                }
        }

    private val animeRecommendationAdapter: RecommendationItemPagingAdapter =
        RecommendationItemPagingAdapter(
            onItemClicked = { recommendationItem ->
                //TODO: Implement Bottom Fragment View for Recommendation Item
            },
            onNextButtonClicked = {
                val currentPosition =
                    (binding.rvRecommendationAnime.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                binding.rvRecommendationAnime.smoothScrollToPosition(currentPosition + 1)
            }
        )

    private fun setUpRecommendation() =
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            val mLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            val snapHelper = LinearSnapHelper()
            binding.rvRecommendationAnime.apply {
                adapter = animeRecommendationAdapter
                layoutManager = mLayoutManager
                addItemDecoration(DividerItemDecoration(context, mLayoutManager.orientation))
                snapHelper.attachToRecyclerView(this)
            }

            viewModel.animeRecommendation.collectLatest { recommendationAnime ->
                animeRecommendationAdapter.submitData(
                    lifecycle = viewLifecycleOwner.lifecycle,
                    pagingData = recommendationAnime
                )
            }
        }

    private fun listenIntoRecommendationAnimeProgress() =
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            val placeHolderRecommendationAdapter = RecommendationPlaceholderAdapter()

            animeRecommendationAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .collectLatest { loadState ->
                    when (loadState.refresh) {
                        is LoadState.NotLoading -> {
                            Timber.d("Current state is Not Loading")
                            TransitionManager.beginDelayedTransition(
                                binding.root,
                                AutoTransition()
                            )
                            binding.rvRecommendationAnime.apply {
                                adapter = animeRecommendationAdapter
                                layoutManager =
                                    LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                            }
                            binding.tvRecommendationPageNumber.apply {
                                text = getString(
                                    R.string.recommendation_page_number,
                                    1,
                                    animeRecommendationAdapter.itemCount
                                )
                                visibility = View.VISIBLE
                            }
                        }
                        LoadState.Loading -> {
                            Timber.d("Current state is Loading")
                            if (binding.rvRecommendationAnime.adapter != placeHolderRecommendationAdapter) {
                                TransitionManager.beginDelayedTransition(binding.root)
                                binding.rvRecommendationAnime.adapter =
                                    placeHolderRecommendationAdapter
                            }
                        }
                        is LoadState.Error -> {
                            Timber.d("Current state is Error")
                            if (binding.rvRecommendationAnime.adapter == placeHolderRecommendationAdapter) {
                                TransitionManager.beginDelayedTransition(
                                    binding.root,
                                    AutoTransition()
                                )
                                binding.rvRecommendationAnime.adapter = RecommendationErrorAdapter(
                                    message = (loadState.refresh as LoadState.Error).error.message
                                        ?: "Unknown",
                                    onRetry = animeRecommendationAdapter::retry
                                )
                            }
                        }
                    }
                }
        }

    private fun listenIntoRecommendationAnimePageNumber() =
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            binding.rvRecommendationAnime.addOnScrollListener(object :
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
                                animeRecommendationAdapter.itemCount
                            )
                    }
                }
            })
        }

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.tag("Fragment Logger").w("Fragment Dashboard Anime View was Destroyed")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.tag("Fragment Logger").w("Fragment Dashboard Anime was Destroyed")
    }
}