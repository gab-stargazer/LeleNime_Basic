package com.lelestacia.lelenimexml.feature.explore

import android.content.Context
import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.viewbinding.library.fragment.viewBinding
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.lelestacia.lelenimexml.core.model.anime.Anime
import com.lelestacia.lelenimexml.feature.common.adapter.ListAnimePagingAdapter
import com.lelestacia.lelenimexml.feature.explore.adapter.ErrorHorizontalAdapter
import com.lelestacia.lelenimexml.feature.explore.adapter.HorizontalAnimePagingAdapter
import com.lelestacia.lelenimexml.feature.explore.adapter.HorizontalFooterLoadStateAdapter
import com.lelestacia.lelenimexml.feature.explore.adapter.PlaceHolderHorizontalAdapter
import com.lelestacia.lelenimexml.feature.explore.databinding.FragmentExploreBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChangedBy
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class ExploreFragment : Fragment(R.layout.fragment_explore), MenuProvider {

    private val binding: FragmentExploreBinding by viewBinding()
    private val viewModel: ExploreViewModel by activityViewModels()
    private val topAnimeAdapter = HorizontalAnimePagingAdapter { anime ->
        navigateToDetail(anime = anime)
    }
    private val airingAnimeAdapter = HorizontalAnimePagingAdapter { anime ->
        navigateToDetail(anime = anime)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            this, viewLifecycleOwner, Lifecycle.State.RESUMED
        )

        val format = "dd MMMM yyyy"
        val simpleDateFormat = SimpleDateFormat(format, Locale.getDefault())
        binding.tvHeaderDate.text = simpleDateFormat.format(Date())

        setTopAnime()
        listenIntoTopAnimeProgress()

        setAiringAnime()
        listenIntoAiringAnimeProgress()

        setHistoryAnime()
    }

    private fun setTopAnime() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        binding.btnMoreTopAnime.setOnClickListener {
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

        viewModel.topAnime.collectLatest { upcomingAnime ->
            topAnimeAdapter.submitData(
                lifecycle = viewLifecycleOwner.lifecycle,
                pagingData = upcomingAnime
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
                        if (binding.shimmerTopAnime.isShimmerVisible) binding.shimmerTopAnime.hideShimmer()
                        TransitionManager.beginDelayedTransition(
                            binding.root,
                            AutoTransition()
                        )
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
        binding.btnMoreAiringAnime.setOnClickListener {
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
                            if (binding.shimmerAiringAnime.isShimmerVisible) binding.shimmerAiringAnime.hideShimmer()
                            TransitionManager.beginDelayedTransition(
                                binding.root,
                                AutoTransition()
                            )
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

    private fun setHistoryAnime() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        val historyAnimeAdapter = ListAnimePagingAdapter { anime ->
            navigateToDetail(anime = anime)
        }

        val mLayoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
        val snapHelper = LinearSnapHelper()

        binding.rvHistoryAnime.apply {
            adapter = historyAnimeAdapter
            layoutManager = mLayoutManager
            addItemDecoration(DividerItemDecoration(context, mLayoutManager.orientation))
            setHasFixedSize(true)
            snapHelper.attachToRecyclerView(this)
        }

        viewModel.historyAnime.collectLatest { historyAnime ->
            historyAnimeAdapter.submitData(
                lifecycle = viewLifecycleOwner.lifecycle,
                pagingData = historyAnime
            )
        }
    }

    private fun navigateToDetail(anime: Anime) = viewLifecycleOwner.lifecycleScope.launch {
        Timber.d("Selected anime was ${anime.title}")
        viewModel.insertOrUpdateAnimeToHistory(anime = anime).join()
        val destination = ExploreFragmentDirections.exploreToDetail(malID = anime.malID)
        findNavController().navigate(destination)
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(com.lelestacia.lelenimexml.feature.common.R.menu.main_menu, menu)
        val myActionMenuItem =
            menu.findItem(com.lelestacia.lelenimexml.feature.common.R.id.btn_search_menu)
        val searchView = myActionMenuItem.actionView as SearchView
        searchView.queryHint =
            getString(com.lelestacia.lelenimexml.feature.common.R.string.insert_anime_title)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(view?.windowToken, 0)
                viewModel.insertNewSearchQuery(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean = false
        })
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean = false
}