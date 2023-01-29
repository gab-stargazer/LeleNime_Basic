package com.lelestacia.lelenimexml.feature.anime.ui.home

import android.content.Context
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.viewbinding.library.fragment.viewBinding
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.lelestacia.lelenimexml.feature.anime.R
import com.lelestacia.lelenimexml.feature.anime.databinding.FragmentAnimeBinding
import com.lelestacia.lelenimexml.feature.anime.ui.adapter.FooterLoadStateAdapter
import com.lelestacia.lelenimexml.feature.anime.ui.adapter.ListAnimePagingAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AnimeFragment : Fragment(R.layout.fragment_anime), MenuProvider, View.OnClickListener {

    private val binding: FragmentAnimeBinding by viewBinding()
    private val viewModel by viewModels<AnimeViewModel>()
    private val animeAdapter = ListAnimePagingAdapter { anime ->
        lifecycleScope.launch {
            viewModel.insertOrUpdateAnimeToHistory(anime).join()
            val action = AnimeFragmentDirections.animeToDetail(anime.animeID)
            findNavController().navigate(action)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(
            this, viewLifecycleOwner, Lifecycle.State.RESUMED
        )
        binding.apply {
            setInformationText()
            setData()
            tvInformation.setOnClickListener(this@AnimeFragment)
        }
    }

    private fun FragmentAnimeBinding.setInformationText() {
        viewModel.searchQuery.observe(viewLifecycleOwner) { searchQuery ->
            if (searchQuery.isEmpty()) {
                tvInformation.text = getString(R.string.this_season)
                tvInformation.setCompoundDrawablesWithIntrinsicBounds(
                    null, null, null, null
                )
                return@observe
            }

            tvInformation.text = getString(R.string.searching_for, searchQuery)
            tvInformation.setCompoundDrawablesWithIntrinsicBounds(
                null, null, ContextCompat.getDrawable(requireContext(), R.drawable.ic_clear), null
            )
        }
    }

    private fun FragmentAnimeBinding.setData() {
        val myLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        rvAnime.apply {
            layoutManager = myLayoutManager
            adapter = animeAdapter.withLoadStateFooter(footer = FooterLoadStateAdapter {
                    animeAdapter.retry()
                })
            addItemDecoration(DividerItemDecoration(context, myLayoutManager.orientation))
            setHasFixedSize(true)
        }

        animeAdapter.loadStateFlow.asLiveData().observe(viewLifecycleOwner) { loadState ->
                with(loadState.refresh) {
                    when (this) {
                        LoadState.Loading -> {
                            binding.showError(
                                isError = false, errorMessage = null
                            )
                            binding.showNotFound(isNotFound = false)

                            if (animeAdapter.itemCount == 0) {
                                binding.showLoading(isLoading = true)
                                return@observe
                            }

                            Snackbar.make(
                                binding.root, getString(R.string.loading), Snackbar.LENGTH_SHORT
                            ).show()
                        }

                        is LoadState.NotLoading -> {
                            binding.showLoading(isLoading = false)
                            if (animeAdapter.itemCount == 0) binding.showNotFound(isNotFound = true)
                        }

                        is LoadState.Error -> {
                            binding.showLoading(isLoading = false)
                            binding.showError(
                                isError = true, errorMessage = error.localizedMessage
                            )
                        }
                    }
                }
            }

        val viewLifecycle = viewLifecycleOwner.lifecycle
        viewModel.getAnimeData.observe(viewLifecycleOwner) { animePagingData ->
                animeAdapter.submitData(viewLifecycle, animePagingData)
            }

        screenError.btnRetry.setOnClickListener {
                animeAdapter.refresh()
            }
    }

    private fun FragmentAnimeBinding.showLoading(isLoading: Boolean) {
        if (isLoading) screenLoading.root.visibility = View.VISIBLE
        else screenLoading.root.visibility = View.GONE
    }

    private fun FragmentAnimeBinding.showError(isError: Boolean, errorMessage: String?) {
        if (isError) {
            screenError.root.visibility = View.VISIBLE
            screenError.tvError.text = errorMessage
        } else screenError.root.visibility = View.GONE
    }

    private fun FragmentAnimeBinding.showNotFound(isNotFound: Boolean) {
        if (isNotFound) screenNotFound.root.visibility = View.VISIBLE
        else screenNotFound.root.visibility = View.GONE
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.main_menu, menu)
        val myActionMenuItem = menu.findItem(R.id.btn_search_menu)
        val searchView = myActionMenuItem.actionView as SearchView
        searchView.queryHint = getString(R.string.insert_anime_title)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(
                        view?.windowToken,
                        0
                    )
                viewModel.insertNewSearchQuery(query)
                binding.rvAnime.smoothScrollToPosition(0)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean = false
        })
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.btn_favorite_menu -> {
                findNavController().navigate(AnimeFragmentDirections.animeToFavorite())
                true
            }

            R.id.btn_history_menu -> {
                findNavController().navigate(AnimeFragmentDirections.animeToHistory())
                true
            }

            else -> false
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.tvInformation.id -> {
                if (binding.tvInformation.text == getString(R.string.this_season)) return
                viewModel.insertNewSearchQuery("")
            }
        }
    }
}
