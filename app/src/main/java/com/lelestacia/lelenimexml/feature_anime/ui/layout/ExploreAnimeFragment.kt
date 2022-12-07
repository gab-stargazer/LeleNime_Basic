package com.lelestacia.lelenimexml.feature_anime.ui.layout

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.lelestacia.lelenimexml.databinding.FragmentExploreAnimeBinding
import com.lelestacia.lelenimexml.feature_anime.ui.viewmodel.AnimeViewModel

class ExploreAnimeFragment : Fragment() {

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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}