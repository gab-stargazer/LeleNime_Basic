package com.lelestacia.lelenimexml.feature.explore

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.lelestacia.lelenimexml.core.model.anime.Anime
import com.lelestacia.lelenimexml.feature.common.adapter.RecommendationPagingDataAdapter
import com.lelestacia.lelenimexml.feature.explore.adapter.*
import com.lelestacia.lelenimexml.feature.explore.databinding.FragmentExploreBinding
import dagger.hilt.android.AndroidEntryPoint
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
    private val topAnimeAdapter = HorizontalAnimePagingAdapter { anime ->
        navigateToDetail(anime = anime)
    }
    private val airingAnimeAdapter = HorizontalAnimePagingAdapter { anime ->
        navigateToDetail(anime = anime)
    }
    private val upcomingAnimeAdapter = HorizontalAnimePagingAdapter { anime ->
        navigateToDetail(anime = anime)
    }
    private val topMangaAdapter = HorizontalMangaPagingAdapter { manga ->

    }
    private val recommendationAnimeAdapter = RecommendationPagingDataAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val format = "dd MMMM yyyy"
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        binding.tvHeaderDate.text = simpleDateFormat.format(Date())

        setTopAnime()
        listenIntoTopAnimeProgress()

        setAiringAnime()
        listenIntoAiringAnimeProgress()

        setUpcomingAnime()
        listenIntoUpcomingAnimeProgress()

        setRecommendationAnime()
        listenIntoRecommendationAnimeProgress()

        setTopManga()
        listenIntoTopMangaProgress()
    }

    private fun setTopAnime() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        binding.cvHeaderTopAnime.setOnClickListener {
            val destination = ExploreFragmentDirections.exploreToExpanded(
                title = getString(R.string.top_anime),
            )
            findNavController().navigate(destination)
        }

        binding.rvTopAnime.apply {
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
        val placeHolderAnimeAdapter = PlaceHolderHorizontalAdapter((1..4).toList())

        topAnimeAdapter.loadStateFlow
            .distinctUntilChangedBy { it.refresh }
            .collectLatest { loadState ->
                when (loadState.refresh) {
                    is LoadState.NotLoading -> {
                        Timber.d("Current state is Not Loading")
                        TransitionManager.beginDelayedTransition(
                            binding.rvTopAnime,
                            AutoTransition()
                        )
                        if (binding.shimmerTopAnime.isShimmerVisible) binding.shimmerTopAnime.hideShimmer()
                        binding.rvTopAnime.apply {
                            adapter = topAnimeAdapter.withLoadStateFooter(
                                HorizontalFooterLoadStateAdapter(topAnimeAdapter::retry)
                            )
                            layoutManager =
                                LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                        }
                    }
                    LoadState.Loading -> {
                        Timber.d("Current state is Loading")
                        if (!binding.shimmerTopAnime.isShimmerVisible) binding.shimmerTopAnime.showShimmer(
                            true
                        )
                        if (binding.rvTopAnime.adapter != placeHolderAnimeAdapter) {
                            TransitionManager.beginDelayedTransition(binding.rvTopAnime)
                            binding.rvTopAnime.adapter = placeHolderAnimeAdapter
                        }
                    }
                    is LoadState.Error -> {
                        Timber.d("Current state is Error")
                        if (binding.rvTopAnime.adapter == placeHolderAnimeAdapter) {
                            if (binding.shimmerTopAnime.isShimmerVisible) binding.shimmerTopAnime.hideShimmer()
                            TransitionManager.beginDelayedTransition(
                                binding.root,
                                AutoTransition()
                            )
                            binding.rvTopAnime.adapter = ErrorHorizontalAdapter(
                                message = (loadState.refresh as LoadState.Error).error.message
                                    ?: "Unknown",
                                onRetryClicked = topAnimeAdapter::retry
                            )
                        }
                    }
                }
            }
    }

    private fun setAiringAnime() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        binding.cvHeaderAiringAnime.setOnClickListener {
            val destination = ExploreFragmentDirections.exploreToExpanded(
                title = getString(R.string.airing_anime),
            )
            findNavController().navigate(destination)
        }

        binding.rvAiringAnime.apply {
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
            val placeHolderAnimeAdapter = PlaceHolderHorizontalAdapter((1..4).toList())

            airingAnimeAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .collectLatest { loadState ->
                    when (loadState.refresh) {
                        is LoadState.NotLoading -> {
                            Timber.d("Current state is Not Loading")
                            TransitionManager.beginDelayedTransition(
                                binding.rvAiringAnime,
                                AutoTransition()
                            )
                            if (binding.shimmerAiringAnime.isShimmerVisible) binding.shimmerAiringAnime.hideShimmer()
                            binding.rvAiringAnime.apply {
                                adapter = airingAnimeAdapter.withLoadStateFooter(
                                    HorizontalFooterLoadStateAdapter(airingAnimeAdapter::retry)
                                )
                                layoutManager =
                                    LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                            }
                        }
                        LoadState.Loading -> {
                            Timber.d("Current state is Loading")
                            if (!binding.shimmerAiringAnime.isShimmerVisible) binding.shimmerAiringAnime.showShimmer(
                                true
                            )
                            if (binding.rvAiringAnime.adapter != placeHolderAnimeAdapter) {
                                TransitionManager.beginDelayedTransition(binding.rvAiringAnime)
                                binding.rvAiringAnime.adapter = placeHolderAnimeAdapter
                            }
                        }
                        is LoadState.Error -> {
                            Timber.d("Current state is Error")
                            if (binding.rvAiringAnime.adapter == placeHolderAnimeAdapter) {
                                if (binding.shimmerAiringAnime.isShimmerVisible) binding.shimmerAiringAnime.hideShimmer()
                                TransitionManager.beginDelayedTransition(
                                    binding.root,
                                    AutoTransition()
                                )
                                binding.rvAiringAnime.adapter = ErrorHorizontalAdapter(
                                    message = (loadState.refresh as LoadState.Error).error.message
                                        ?: "Unknown",
                                    onRetryClicked = airingAnimeAdapter::retry
                                )
                            }
                        }
                    }
                }
        }

    private fun setUpcomingAnime() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        binding.cvHeaderUpcomingAnime.setOnClickListener {
            val destination = ExploreFragmentDirections.exploreToExpanded(
                title = getString(R.string.upcoming_anime),
            )
            findNavController().navigate(destination)
        }

        binding.rvUpcomingAnime.apply {
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
            val placeHolderAnimeAdapter = PlaceHolderHorizontalAdapter((1..4).toList())

            upcomingAnimeAdapter.loadStateFlow
                .distinctUntilChangedBy { it.refresh }
                .collectLatest { loadState ->
                    when (loadState.refresh) {
                        is LoadState.NotLoading -> {
                            Timber.d("Current state is Not Loading")
                            TransitionManager.beginDelayedTransition(
                                binding.rvUpcomingAnime,
                                AutoTransition()
                            )
                            if (binding.shimmerUpcomingAnime.isShimmerVisible) binding.shimmerUpcomingAnime.hideShimmer()
                            binding.rvUpcomingAnime.apply {
                                adapter = upcomingAnimeAdapter.withLoadStateFooter(
                                    HorizontalFooterLoadStateAdapter(upcomingAnimeAdapter::retry)
                                )
                                layoutManager =
                                    LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
                            }
                        }
                        LoadState.Loading -> {
                            Timber.d("Current state is Loading")
                            if (!binding.shimmerUpcomingAnime.isShimmerVisible) binding.shimmerUpcomingAnime.showShimmer(
                                true
                            )
                            if (binding.rvUpcomingAnime.adapter != placeHolderAnimeAdapter) {
                                TransitionManager.beginDelayedTransition(binding.rvUpcomingAnime)
                                binding.rvUpcomingAnime.adapter = placeHolderAnimeAdapter
                            }
                        }
                        is LoadState.Error -> {
                            Timber.d("Current state is Error")
                            if (binding.rvAiringAnime.adapter == placeHolderAnimeAdapter) {
                                if (binding.shimmerUpcomingAnime.isShimmerVisible) binding.shimmerUpcomingAnime.hideShimmer()
                                TransitionManager.beginDelayedTransition(
                                    binding.rvUpcomingAnime,
                                    AutoTransition()
                                )
                                binding.rvUpcomingAnime.adapter = ErrorHorizontalAdapter(
                                    message = (loadState.refresh as LoadState.Error).error.message
                                        ?: "Unknown",
                                    onRetryClicked = upcomingAnimeAdapter::retry
                                )
                            }
                        }
                    }
                }
        }

    private fun setTopManga() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        binding.cvHeaderTopManga.setOnClickListener {
            val destination = ExploreFragmentDirections.exploreToExpanded(
                title = getString(R.string.top_manga),
            )
            findNavController().navigate(destination)
        }

        binding.rvTopManga.apply {
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

    private fun listenIntoTopMangaProgress() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        val placeHolderAnimeAdapter = PlaceHolderHorizontalAdapter((1..4).toList())

        topMangaAdapter.loadStateFlow
            .distinctUntilChangedBy { it.refresh }
            .collectLatest { loadState ->
                when (loadState.refresh) {
                    is LoadState.NotLoading -> {
                        Timber.d("Current state is Not Loading")
                        TransitionManager.beginDelayedTransition(
                            binding.rvTopManga,
                            AutoTransition()
                        )
                        if (binding.shimmerTopManga.isShimmerVisible) binding.shimmerTopManga.hideShimmer()
                        binding.rvTopManga.apply {
                            adapter = topMangaAdapter.withLoadStateFooter(
                                HorizontalFooterLoadStateAdapter(topMangaAdapter::retry)
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
                        if (binding.rvTopManga.adapter != placeHolderAnimeAdapter) {
                            TransitionManager.beginDelayedTransition(binding.rvTopManga)
                            binding.rvTopManga.adapter = placeHolderAnimeAdapter
                        }
                    }
                    is LoadState.Error -> {
                        Timber.d("Current state is Error")
                        if (binding.rvTopManga.adapter == placeHolderAnimeAdapter) {
                            if (binding.shimmerTopManga.isShimmerVisible) binding.shimmerTopManga.hideShimmer()
                            TransitionManager.beginDelayedTransition(
                                binding.rvTopManga,
                                AutoTransition()
                            )
                            binding.rvTopManga.adapter = ErrorHorizontalAdapter(
                                message = (loadState.refresh as LoadState.Error).error.message
                                    ?: "Unknown",
                                onRetryClicked = topMangaAdapter::retry
                            )
                        }
                    }
                }
            }
    }

    private fun setRecommendationAnime() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
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

    private fun listenIntoRecommendationAnimeProgress() =
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {

        }

    private fun navigateToDetail(anime: Anime) = viewLifecycleOwner.lifecycleScope.launch {
        Timber.d("Selected anime was ${anime.title}")
        viewModel.insertOrUpdateAnimeToHistory(anime = anime).join()
        val destination = ExploreFragmentDirections.exploreToDetail(malID = anime.malID)
        findNavController().navigate(destination)
    }
}