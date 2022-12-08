package com.lelestacia.lelenimexml.feature_anime.ui.layout

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lelestacia.lelenimexml.R
import com.lelestacia.lelenimexml.databinding.FragmentExploreAnimeBinding
import com.lelestacia.lelenimexml.feature_anime.ui.adapter.AnimeRowPagingAdapter
import com.lelestacia.lelenimexml.feature_anime.ui.adapter.FooterLoadStateAdapter
import com.lelestacia.lelenimexml.feature_anime.ui.adapter.HeaderLoadStateAdapter
import com.lelestacia.lelenimexml.feature_anime.ui.viewmodel.AnimeViewModel
import kotlinx.coroutines.Dispatchers

class ExploreAnimeFragment : Fragment(), MenuProvider, SearchView.OnQueryTextListener {

    private val viewModel by activityViewModels<AnimeViewModel>()
    private var _binding: FragmentExploreAnimeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExploreAnimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pagingAdapter = AnimeRowPagingAdapter {
            val action = ExploreAnimeFragmentDirections.exploreToDetail(it)
            view.findNavController().navigate(action)
        }
        binding.rvAnimeResult.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pagingAdapter.withLoadStateHeaderAndFooter(
                header = HeaderLoadStateAdapter {
                    pagingAdapter.retry()
                },
                footer = FooterLoadStateAdapter {
                    pagingAdapter.retry()
                }
            )
            setHasFixedSize(true)
        }
        viewModel.searchAnimeByTitle()
            .asLiveData(Dispatchers.Main)
            .observe(viewLifecycleOwner) {
                pagingAdapter.submitData(lifecycle, it)
            }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menuInflater.inflate(R.menu.explore, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        when (menuItem.itemId) {
            R.id.btn_search_menu -> {
                Log.d(TAG, "onMenuItemSelected: Clicked")
            }
        }
        return true
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        Log.d(TAG, "onQueryTextSubmit: $query")
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }
}