package com.lelestacia.lelenimexml.feature_anime.ui.season_anime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.lelestacia.lelenimexml.feature_anime.databinding.FragmentSeasonAnimeBinding
import com.lelestacia.lelenimexml.feature_anime.ui.adapter.FooterLoadStateAdapter
import com.lelestacia.lelenimexml.feature_anime.ui.adapter.ListAnimeAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SeasonAnimeFragment : Fragment() {

    private val viewModel by viewModels<SeasonAnimeViewModel>()
    private var _binding: FragmentSeasonAnimeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSeasonAnimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val seasonAnimeAdapter = ListAnimeAdapter { anime ->
            val animeEntity = anime.toAnimeEntity()
            viewModel.insertOrUpdateNewAnimeToHistory(animeEntity)
            val action = SeasonAnimeFragmentDirections.airingToDetail(anime)
            findNavController().navigate(action)
        }

        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        binding.rvSeasonAnime
            .apply {
                this.layoutManager = layoutManager
                adapter = seasonAnimeAdapter.withLoadStateFooter(
                    footer = FooterLoadStateAdapter {
                        seasonAnimeAdapter.retry()
                    }
                )
                addItemDecoration(DividerItemDecoration(context, layoutManager.orientation))
                setHasFixedSize(true)
            }

        seasonAnimeAdapter
            .loadStateFlow
            .asLiveData()
            .observe(viewLifecycleOwner) { loadState ->
                if (loadState.refresh is LoadState.Loading) {
                    binding.showLoading(isLoading = true)
                } else binding.showLoading(isLoading = false)

                if (loadState.refresh is LoadState.Error) {
                    val error = (loadState.refresh as LoadState.Error).error
                    binding.showError(true, error.localizedMessage)

                } else {
                    binding.showError(isError = false, errorMessage = null)
                }
            }

        viewModel
            .airingAnime
            .observe(viewLifecycleOwner) { airingAnimePagingData ->
                seasonAnimeAdapter.submitData(lifecycle, airingAnimePagingData)
            }

        binding.btnRetry.setOnClickListener {
            seasonAnimeAdapter.refresh()
        }
    }

    private fun FragmentSeasonAnimeBinding.showLoading(isLoading: Boolean) {
        if (isLoading) {
            progressCircular.visibility = View.VISIBLE
            tvLoading.visibility = View.VISIBLE
        } else {
            progressCircular.visibility = View.GONE
            tvLoading.visibility = View.GONE
        }
    }

    private fun FragmentSeasonAnimeBinding.showError(isError: Boolean, errorMessage: String?) {
        if (isError) {
            btnRetry.visibility = View.VISIBLE
            tvError.text = errorMessage
            tvError.visibility = View.VISIBLE
        } else {
            btnRetry.visibility = View.GONE
            tvError.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}