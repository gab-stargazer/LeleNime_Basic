package com.lelestacia.lelenimexml.feature.explore

import android.os.Bundle
import android.transition.TransitionManager
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lelestacia.lelenimexml.feature.common.adapter.anime.AnimeHorizontalPagingAdapter
import com.lelestacia.lelenimexml.feature.common.adapter.util.HorizontalErrorAdapter
import com.lelestacia.lelenimexml.feature.common.adapter.util.HorizontalLoadStateAdapter
import com.lelestacia.lelenimexml.feature.explore.adapter.PlaceHolderHorizontalAdapter
import com.lelestacia.lelenimexml.feature.explore.databinding.FragmentDashboardAnimeBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class DashboardAnimeFragment : Fragment(R.layout.fragment_dashboard_anime) {

    private val viewModel: DashboardAnimeViewModel by viewModels()
    private val binding: FragmentDashboardAnimeBinding by viewBinding()
    private val placeHolderAdapter: PlaceHolderHorizontalAdapter =
        PlaceHolderHorizontalAdapter(placeHolderCount = (1..4).toList())
    private val topAnimeAdapter = AnimeHorizontalPagingAdapter { anime ->

    }
    private val airingAnimeAdapter = AnimeHorizontalPagingAdapter { anime ->
    }
    private val upcomingAnimeAdapter = AnimeHorizontalPagingAdapter { anime ->
    }


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
    }


    private fun setTopAnime() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        binding.layoutTrendingAnime.setOnClickListener {
            val destination =
                DashboardFragmentDirections.exploreToExpanded(getString(R.string.top_anime))
            findNavController().navigate(destination)
        }
        val rvTopAnime = binding.rvTopAnime
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
        val rvTopAnime = binding.rvTopAnime
        val shimmerTopAnime = binding.shimmerTopAnime
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

    private fun setAiringAnime() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        val rvAiringAnime = binding.rvAiringAnime
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

    private fun setUpcomingAnime() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        val rvUpcomingAnime = binding.rvUpcomingAnime
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

    override fun onDestroyView() {
        super.onDestroyView()
        Timber.tag("Fragment Logger").w("Fragment Dashboard Anime View was Destroyed")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.tag("Fragment Logger").w("Fragment Dashboard Anime was Destroyed")
    }
}