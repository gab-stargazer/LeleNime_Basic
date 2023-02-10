package com.lelestacia.lelenimexml.feature.collection

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.lelestacia.lelenimexml.feature.collection.databinding.FragmentCollectionBinding
import com.lelestacia.lelenimexml.feature.common.adapter.ListAnimePagingAdapterCollection
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class CollectionFragment : Fragment(R.layout.fragment_collection) {

    private val binding: FragmentCollectionBinding by viewBinding()
    private val viewModel: CollectionViewModel by activityViewModels()
    private lateinit var animeAdapter: ListAnimePagingAdapterCollection


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val myLayoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        animeAdapter = ListAnimePagingAdapterCollection { anime ->
            Timber.d("Clicked anime was ${anime.title} and isFavorite: ${anime.isFavorite}")
            val destination = CollectionFragmentDirections.collectionToDetail(malID = anime.malID)
            findNavController().navigate(destination)
        }
        binding.rvCollection.apply {
            adapter = animeAdapter
            layoutManager = myLayoutManager
            addItemDecoration(DividerItemDecoration(context, myLayoutManager.orientation))
            setHasFixedSize(true)
        }
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.historyAnime.collect {
                animeAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }
    }
}