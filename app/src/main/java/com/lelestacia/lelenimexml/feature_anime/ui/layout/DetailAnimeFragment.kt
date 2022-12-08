package com.lelestacia.lelenimexml.feature_anime.ui.layout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.lelestacia.lelenimexml.core.utililty.NetworkResponse
import com.lelestacia.lelenimexml.databinding.FragmentDetailAnimeBinding
import com.lelestacia.lelenimexml.feature_anime.ui.adapter.TitleAdapter
import com.lelestacia.lelenimexml.feature_anime.ui.viewmodel.AnimeViewModel

class DetailAnimeFragment : Fragment() {

    private val viewModel by activityViewModels<AnimeViewModel>()
    private val args: DetailAnimeFragmentArgs by navArgs()
    private var _binding: FragmentDetailAnimeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailAnimeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = TitleAdapter()
        viewModel.getAnimeById(args.animeID)
        viewModel.anime.observe(viewLifecycleOwner) {
            when (it) {
                is NetworkResponse.GenericException -> Snackbar.make(
                    view,
                    "${it.code} : ${it.cause}",
                    Snackbar.LENGTH_LONG
                ).show()
                NetworkResponse.Loading -> Unit
                NetworkResponse.NetworkException -> Snackbar.make(
                    view,
                    "No Internet sir, are u from north korea?",
                    Snackbar.LENGTH_LONG
                ).show()
                is NetworkResponse.Success -> {
                    binding.apply {
                        rvTitle.adapter = adapter
                        rvTitle.layoutManager = LinearLayoutManager(requireContext())
                        ivCoverAnime.load(it.data.data.images.webp.largeImageUrl)
                        it.data.data.titles
                            .filterNot { it.type.contains("Default") }
                            .filterNot { it.type.contains("Synonym") }
                            .also { title ->
                            adapter.submitList(title)
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}