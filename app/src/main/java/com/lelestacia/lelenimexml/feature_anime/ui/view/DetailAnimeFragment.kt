package com.lelestacia.lelenimexml.feature_anime.ui.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.lelestacia.lelenimexml.databinding.FragmentDetailAnimeBinding

class DetailAnimeFragment : Fragment() {

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
        binding.apply {
            ivCoverAnime.load(args.anime.images)
            tvAnimeTitle.text = args.anime.title
            if (args.anime.titleJapanese != null)
                tvAnimeTitleJapanese.text = args.anime.titleJapanese
            else
                tvAnimeTitle.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}