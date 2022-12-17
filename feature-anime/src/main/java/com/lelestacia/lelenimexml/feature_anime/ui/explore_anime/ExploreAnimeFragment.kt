package com.lelestacia.lelenimexml.feature_anime.ui.explore_anime

import android.content.Context
import android.os.Bundle
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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.lelestacia.lelenimexml.feature_anime.R
import com.lelestacia.lelenimexml.feature_anime.databinding.FragmentExploreAnimeBinding
import com.lelestacia.lelenimexml.feature_anime.ui.adapter.FooterLoadStateAdapter
import com.lelestacia.lelenimexml.feature_anime.ui.adapter.HeaderLoadStateAdapter
import com.lelestacia.lelenimexml.feature_anime.ui.adapter.ListAnimePagingAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExploreAnimeFragment : Fragment(R.layout.fragment_explore_anime), MenuProvider {

    private val viewModel by viewModels<ExploreAnimeViewModel>()
    private val binding: FragmentExploreAnimeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()

        val exploreAnimeAdapter = ListAnimePagingAdapter { anime ->
            viewModel.insertNewOrUpdateLastViewed(anime = anime)
            val action = ExploreAnimeFragmentDirections.exploreToDetail(anime)
            view.findNavController().navigate(action)
        }

        val myLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        exploreAnimeAdapter
            .loadStateFlow
            .asLiveData()
            .observe(viewLifecycleOwner) { loadState ->
                with(loadState.refresh) {
                    when (this) {
                        is LoadState.Loading -> {
                            binding.showNotFound(isNotFound = false)
                            binding.showError(false, errorMessage = null)

                            if (exploreAnimeAdapter.itemCount == 0) {
                                binding.showLoading(isLoading = true)
                                return@observe
                            }

                            Snackbar
                                .make(view, getString(R.string.loading), Snackbar.LENGTH_SHORT)
                                .show()
                        }
                        is LoadState.NotLoading -> {
                            binding.showLoading(isLoading = false)
                            if (exploreAnimeAdapter.itemCount == 0)
                                binding.showNotFound(isNotFound = true)
                            else binding.showNotFound(isNotFound = false)
                        }
                        is LoadState.Error -> {
                            binding.showLoading(isLoading = false)
                            binding.showError(isError = true, error.localizedMessage)
                        }
                    }
                }
            }

        binding.rvAnimeResult.apply {
            layoutManager = myLayoutManager
            adapter = exploreAnimeAdapter.withLoadStateHeaderAndFooter(
                header = HeaderLoadStateAdapter {
                    exploreAnimeAdapter.retry()
                },
                footer = FooterLoadStateAdapter {
                    exploreAnimeAdapter.retry()
                }
            )
            addItemDecoration(DividerItemDecoration(context, myLayoutManager.orientation))
            setHasFixedSize(true)
        }

        viewModel
            .searchAnimeByTitle
            .observe(viewLifecycleOwner) { anime ->
                exploreAnimeAdapter.submitData(lifecycle, anime)
            }

        binding.btnRetry
            .setOnClickListener {
                exploreAnimeAdapter.refresh()
            }
    }

    private fun FragmentExploreAnimeBinding.showLoading(isLoading: Boolean) {
        if (isLoading) {
            progressCircular.visibility = View.VISIBLE
            tvLoading.visibility = View.VISIBLE
        } else {
            progressCircular.visibility = View.GONE
            tvLoading.visibility = View.GONE
        }
    }

    private fun FragmentExploreAnimeBinding.showError(isError: Boolean, errorMessage: String?) {
        if (isError) {
            btnRetry.visibility = View.VISIBLE
            tvError.text = errorMessage
            tvError.visibility = View.VISIBLE
        } else {
            btnRetry.visibility = View.GONE
            tvError.visibility = View.GONE
        }
    }

    private fun FragmentExploreAnimeBinding.showNotFound(isNotFound: Boolean) {
        if (isNotFound) {
            ivNotFound.visibility = View.VISIBLE
            tvNotFound.visibility = View.VISIBLE
        } else {
            ivNotFound.visibility = View.GONE
            tvNotFound.visibility = View.GONE
        }
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost
            .addMenuProvider(
                this,
                viewLifecycleOwner,
                Lifecycle.State.RESUMED
            )
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.explore, menu)
        val myActionMenuItem = menu.findItem(R.id.btn_search_menu)
        val searchView = myActionMenuItem.actionView as SearchView?
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                    .hideSoftInputFromWindow(view?.windowToken, 0)

                viewModel.searchAnime(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean = false
        })
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean = false
}