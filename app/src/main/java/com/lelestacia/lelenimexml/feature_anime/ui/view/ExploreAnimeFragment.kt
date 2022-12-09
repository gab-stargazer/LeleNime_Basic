package com.lelestacia.lelenimexml.feature_anime.ui.view

import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.lelestacia.lelenimexml.databinding.FragmentExploreAnimeBinding
import com.lelestacia.lelenimexml.feature_anime.ui.adapter.AnimePagingAdapter
import com.lelestacia.lelenimexml.feature_anime.ui.adapter.FooterLoadStateAdapter
import com.lelestacia.lelenimexml.feature_anime.ui.adapter.HeaderLoadStateAdapter
import com.lelestacia.lelenimexml.feature_anime.ui.viewmodel.AnimeViewModel

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
        val pagingAdapter = AnimePagingAdapter { anime ->
            val action = ExploreAnimeFragmentDirections.exploreToDetail(anime)
            findNavController().navigate(action)
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
            .observe(viewLifecycleOwner) { anime ->
                pagingAdapter.submitData(lifecycle, anime)
            }
    }

    private fun setupMenu() {
        val menuHost : MenuHost = requireActivity()
        menuHost.addMenuProvider(
                this, viewLifecycleOwner, Lifecycle.State.RESUMED
        )
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
//        menuInflater.inflate(R.menu.explore, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return true
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        return false
    }
}