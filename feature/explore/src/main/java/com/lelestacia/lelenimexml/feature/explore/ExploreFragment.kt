package com.lelestacia.lelenimexml.feature.explore

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.lelestacia.lelenimexml.core.model.anime.Anime
import com.lelestacia.lelenimexml.feature.common.adapter.anime.AnimeHorizontalPagingAdapter
import com.lelestacia.lelenimexml.feature.common.adapter.recommendation.RecommendationAnimePagingAdapter
import com.lelestacia.lelenimexml.feature.common.adapter.recommendation.RecommendationErrorAdapter
import com.lelestacia.lelenimexml.feature.common.adapter.recommendation.RecommendationPlaceholderAdapter
import com.lelestacia.lelenimexml.feature.common.adapter.util.HorizontalErrorAdapter
import com.lelestacia.lelenimexml.feature.common.adapter.util.HorizontalLoadStateAdapter
import com.lelestacia.lelenimexml.feature.explore.adapter.HorizontalMangaPagingAdapter
import com.lelestacia.lelenimexml.feature.explore.adapter.PlaceHolderHorizontalAdapter
import com.lelestacia.lelenimexml.feature.explore.databinding.FragmentExploreBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ExploreFragment : Fragment(R.layout.fragment_explore) {

    private val binding: FragmentExploreBinding by viewBinding()
    private val viewModel: ExploreViewModel by activityViewModels()
    private val placeHolderAdapter: PlaceHolderHorizontalAdapter =
        PlaceHolderHorizontalAdapter(placeHolderCount = (1..4).toList())
    private val topAnimeAdapter = AnimeHorizontalPagingAdapter { anime ->
        navigateToDetail(anime = anime)
    }
    private val airingAnimeAdapter = AnimeHorizontalPagingAdapter { anime ->
        navigateToDetail(anime = anime)
    }
    private val upcomingAnimeAdapter = AnimeHorizontalPagingAdapter { anime ->
        navigateToDetail(anime = anime)
    }
    private val topMangaAdapter = HorizontalMangaPagingAdapter { manga ->

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val format = "dd MMMM yyyy"
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        binding.tvHeaderDate.text = simpleDateFormat.format(Date())

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            setTopAnime()
            listenIntoTopAnimeProgress()

            delay(250L)
            setAiringAnime()
            listenIntoAiringAnimeProgress()

            delay(500L)
            setUpcomingAnime()
            listenIntoUpcomingAnimeProgress()

            delay(750L)
            setTopManga()
            listenIntoTopMangaProgress()
        }

        setRecommendationAnime()
        listenIntoRecommendationAnimeProgress()
        listenIntoRecommendationAnimePageNumber()

    }

        private val rvTopAnime = binding.rvTopAnime
        private val shimmerTopAnime = binding.shimmerTopAnime
        private fun setTopAnime() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            binding.cvHeaderTopAnime.setOnClickListener {

            }

            rvTopAnime.apply {
                layoutManager = object : LinearLayoutManager(context, RecyclerView.HORIZONTAL, false) {
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

        private fun listenIntoTopAnimeProgress() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
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

        private val rvAiringAnime = binding.rvAiringAnime
        private val shimmerAiringAnime = binding.shimmerAiringAnime
        private fun setAiringAnime() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            binding.cvHeaderAiringAnime.setOnClickListener {

            }

            rvAiringAnime.apply {
                layoutManager = object : LinearLayoutManager(context, RecyclerView.HORIZONTAL, false) {
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

        private val rvUpcomingAnime = binding.rvUpcomingAnime
        private val shimmerUpcomingAnime = binding.shimmerUpcomingAnime
        private fun setUpcomingAnime() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            binding.cvHeaderUpcomingAnime.setOnClickListener {

            }

            rvUpcomingAnime.apply {
                layoutManager = object : LinearLayoutManager(context, RecyclerView.HORIZONTAL, false) {
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


    private val rvTopManga = binding.rvTopManga
    private val shimmerTopManga = binding.shimmerTopManga
    private fun setTopManga() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        binding.cvHeaderTopManga.setOnClickListener {

        }

        rvTopManga.apply {
            layoutManager = object : LinearLayoutManager(context, RecyclerView.HORIZONTAL, false) {
                override fun canScrollHorizontally(): Boolean = false
            }
            setHasFixedSize(true)
        }

        viewModel.topManga.collectLatest { upcomingAnime ->
            topMangaAdapter.submitData(
                lifecycle = viewLifecycleOwner.lifecycle,
                pagingData = upcomingAnime
            )
        }
    }

    private fun listenIntoTopMangaProgress() =
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            topMangaAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .collectLatest { loadState ->
                    when (loadState.refresh) {
                        is LoadState.NotLoading -> {
                            Timber.d("Current state is Not Loading")
                            TransitionManager.beginDelayedTransition(shimmerTopManga)
                            if (shimmerTopManga.isShimmerVisible) shimmerTopManga.hideShimmer()
                            rvTopManga.apply {
                                adapter = topMangaAdapter.withLoadStateFooter(
                                    HorizontalLoadStateAdapter(onRetryClicked = topMangaAdapter::retry)
                                )
                                layoutManager =
                                    LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                            }
                        }
                        LoadState.Loading -> {
                            Timber.d("Current state is Loading")
                            if (!shimmerTopManga.isShimmerVisible) shimmerTopManga.showShimmer(
                                true
                            )
                            if (rvTopManga.adapter != placeHolderAdapter) {
                                TransitionManager.beginDelayedTransition(rvTopManga)
                                rvTopManga.adapter = placeHolderAdapter
                            }
                        }
                        is LoadState.Error -> {
                            Timber.d("Current state is Error")
                            if (rvTopManga.adapter == placeHolderAdapter) {
                                if (shimmerTopManga.isShimmerVisible) shimmerTopManga.hideShimmer()
                                TransitionManager.beginDelayedTransition(binding.root)
                                rvTopManga.adapter = HorizontalErrorAdapter(
                                    message = (loadState.refresh as LoadState.Error).error.message
                                        ?: "Unknown",
                                    onRetryClicked = topMangaAdapter::retry
                                )
                            }
                        }
                    }
                }
        }

    private val recommendationAnimeAdapter: RecommendationAnimePagingAdapter =
        RecommendationAnimePagingAdapter(
            onItemClicked = { recommendationItem ->
                Timber.d("Selected Item is: $recommendationItem")
            },
            onNextButtonClicked = {
                val currentPosition =
                    (binding.rvRecommendationAnime.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                binding.rvRecommendationAnime.smoothScrollToPosition(currentPosition + 1)
            }
        )

    private fun setRecommendationAnime(): Job =
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            val mLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            val snapHelper = LinearSnapHelper()
            binding.rvRecommendationAnime.apply {
                adapter = recommendationAnimeAdapter
                layoutManager = mLayoutManager
                addItemDecoration(DividerItemDecoration(context, mLayoutManager.orientation))
                snapHelper.attachToRecyclerView(this)
            }

            viewModel.recommendationAnime.collectLatest { recommendationAnime ->
                recommendationAnimeAdapter.submitData(
                    lifecycle = viewLifecycleOwner.lifecycle,
                    pagingData = recommendationAnime
                )
            }
        }

    private fun listenIntoRecommendationAnimeProgress(): Job =
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            val placeHolderRecommendationAdapter = RecommendationPlaceholderAdapter()

            recommendationAnimeAdapter.loadStateFlow
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
                                adapter = recommendationAnimeAdapter
                                layoutManager =
                                    LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                            }
                            binding.tvRecommendationPageNumber.apply {
                                text = getString(
                                    R.string.recommendation_page_number,
                                    1,
                                    recommendationAnimeAdapter.itemCount
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
                                    onRetry = recommendationAnimeAdapter::retry
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
                                recommendationAnimeAdapter.itemCount
                            )
                    }
                }
            })
        }

    private fun navigateToDetail(anime: Anime) = viewLifecycleOwner.lifecycleScope.launch {
        Timber.d("Selected anime was ${anime.title}")
        viewModel.insertOrUpdateAnimeToHistory(anime = anime).join()

    }
}