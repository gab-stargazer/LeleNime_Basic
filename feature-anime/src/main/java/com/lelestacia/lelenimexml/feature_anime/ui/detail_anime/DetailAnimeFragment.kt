package com.lelestacia.lelenimexml.feature_anime.ui.detail_anime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import com.lelestacia.lelenimexml.feature_anime.databinding.FragmentDetailAnimeBinding
import jp.wasabeef.transformers.coil.BlurTransformation

class DetailAnimeFragment : Fragment() {

    private val args by navArgs<DetailAnimeFragmentArgs>()
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
            ivBackgroundAnime.load(args.anime.images) {
                transformations(BlurTransformation(requireContext()))
                build()
            }
            ivCoverAnime.load(args.anime.images)
            tvAnimeTitle.text = args.anime.title
            if (args.anime.titleJapanese != null)
                tvAnimeTitleJapanese.text = args.anime.titleJapanese
            else
                tvAnimeTitle.visibility = View.GONE
            tvTypeValue.text = args.anime.type
            tvEpisodeValue.text =
                if (args.anime.episodes != null) args.anime.episodes.toString()
                else "Unknown"
            tvGenreValue.text = getGenreAsString(args.anime.genres)
            tvStatusValue.text = args.anime.status
            tvSynopsis.text = args.anime.synopsis
        }
    }

    private fun getGenreAsString(genre: List<String>): String {
        var newGenre = ""
        if(genre.isEmpty()) return "Empty Genre"

        for (i in genre.indices) {
            newGenre += if (i == 0) genre[i]
            else ", ${genre[i]}"
        }
        return newGenre
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}