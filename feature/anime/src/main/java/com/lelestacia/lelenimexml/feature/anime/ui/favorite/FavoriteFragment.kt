package com.lelestacia.lelenimexml.feature.anime.ui.favorite

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.lelestacia.lelenimexml.feature.anime.R
import com.lelestacia.lelenimexml.feature.anime.databinding.FragmentFavoriteBinding
import com.lelestacia.lelenimexml.feature.anime.ui.adapter.ListAnimePagingAdapterExtended
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteFragment : Fragment(R.layout.fragment_favorite) {

    private val binding: FragmentFavoriteBinding by viewBinding()
    private val viewModel: FavoriteViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val favoriteAnimeAdapter = ListAnimePagingAdapterExtended(
            onItemClicked = { anime ->
                viewModel.insertOrUpdateAnime(anime)
                val action = FavoriteFragmentDirections.favoriteToDetail(anime.animeID)
                findNavController().navigate(action)
            }, onItemLongClicked = { anime ->
            val action = FavoriteFragmentDirections.favoritePopupMenu(anime)
            findNavController().navigate(action)
        }
        )

        val myLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.rvAnime.apply {
            layoutManager = myLayoutManager
            adapter = favoriteAnimeAdapter
            addItemDecoration(DividerItemDecoration(context, myLayoutManager.orientation))
            setHasFixedSize(true)
        }

        val viewLifecycle = viewLifecycleOwner.lifecycle
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.favoriteAnime.collect { favoriteAnime ->
                favoriteAnimeAdapter.submitData(viewLifecycle, favoriteAnime)
            }
        }
    }
}
