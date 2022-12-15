package com.lelestacia.lelenimexml.feature_anime.ui.detail_anime

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.material.snackbar.Snackbar
import com.lelestacia.lelenimexml.feature_anime.R
import com.lelestacia.lelenimexml.feature_anime.databinding.FragmentDetailAnimeBinding
import com.lelestacia.lelenimexml.feature_anime.ui.adapter.CharacterAdapter
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.transformers.coil.BlurTransformation
import kotlinx.coroutines.flow.catch
import timber.log.Timber

@AndroidEntryPoint
class DetailAnimeFragment : Fragment() {

    private val viewModel by viewModels<DetailAnimeViewModel>()
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
            setHeaderSection()
            setBodySection()
            setCharacterView()
        }
    }

    private fun FragmentDetailAnimeBinding.setCharacterView() {
        val characterAdapter = CharacterAdapter()
        rvCharacter.adapter = characterAdapter
        rvCharacter.setHasFixedSize(true)
        rvCharacter.layoutManager =
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)

        viewModel.getAnimeCharactersById(args.anime.malId).catch { t ->
            Snackbar.make(
                root,
                t.localizedMessage ?: "Something Went Wrong",
                Snackbar.LENGTH_LONG
            ).show()
        }.asLiveData()
            .observe(viewLifecycleOwner) { characters ->
                characterAdapter.submitList(characters)
                Timber.d("Character Count: ${characters.size}")
            }
    }

    private fun FragmentDetailAnimeBinding.setHeaderSection() {
        val anime = args.anime
        ivBackgroundAnime.load(anime.images) {
            transformations(BlurTransformation(requireContext()))
            build()
        }
        ivCoverAnime.load(anime.images)
        tvAnimeTitle.text = anime.title
        if (anime.titleJapanese != null)
            tvAnimeTitleJapanese.text = anime.titleJapanese
        else
            tvAnimeTitle.visibility = View.GONE
        tvRankAndRating.text =
            getString(
                R.string.rank_and_score,
                anime.rank.toString(),
                anime.score ?: "Unknown"
            )
    }

    private fun FragmentDetailAnimeBinding.setBodySection() {
        val anime = args.anime
        tvTypeValue.text = getString(R.string.information_value, anime.type)
        tvEpisodeValue.text =
            if (anime.episodes != null) getString(
                R.string.information_value,
                anime.episodes.toString()
            )
            else getString(R.string.information_value, "Unknown")
        tvGenreValue.text =
            getString(R.string.information_value, getGenreAsString(anime.genres))
        tvStatusValue.text = getString(R.string.information_value, anime.status)
        tvAiredValue.text = getString(
            R.string.information_value,
            getAiredSeason(anime.season, anime.year)
        )
        tvSynopsis.text = anime.synopsis ?: "No Synopsis Provided By The MAL"
    }

    private fun getGenreAsString(genre: List<String>): String {
        var newGenre = ""
        if (genre.isEmpty()) return "Empty Genre"

        for (i in genre.indices) {
            newGenre += if (i == 0) genre[i]
            else ", ${genre[i]}"
        }
        return newGenre
    }

    private fun getAiredSeason(season: String?, year: Int): String {
        return if (season.isNullOrEmpty()) "Unknown"
        else "$season $year"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}