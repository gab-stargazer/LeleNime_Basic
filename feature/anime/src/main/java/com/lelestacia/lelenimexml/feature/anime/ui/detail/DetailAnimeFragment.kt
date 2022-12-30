package com.lelestacia.lelenimexml.feature.anime.ui.detail

import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.snackbar.Snackbar
import com.lelestacia.lelenimexml.core.common.Constant.UNKNOWN
import com.lelestacia.lelenimexml.feature.anime.R
import com.lelestacia.lelenimexml.feature.anime.databinding.FragmentDetailAnimeBinding
import com.lelestacia.lelenimexml.feature.anime.util.ListToString
import com.lelestacia.lelenimexml.feature.anime.ui.adapter.CharacterAdapter
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.transformers.coil.BlurTransformation
import kotlinx.coroutines.flow.catch

@AndroidEntryPoint
class DetailAnimeFragment : Fragment(R.layout.fragment_detail_anime), View.OnClickListener {

    private val viewModel by viewModels<DetailAnimeViewModel>()
    private val args by navArgs<DetailAnimeFragmentArgs>()
    private val binding: FragmentDetailAnimeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            setView()
            setCharacters()
            fabFavorite.setOnClickListener(this@DetailAnimeFragment)
        }
    }

    private fun FragmentDetailAnimeBinding.setView() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.getAnimeByMalId(args.malID).collect { anime ->
                /*Header Section*/
                ivBackgroundAnime.load(anime.coverImages) {
                    transformations(BlurTransformation(requireContext()))
                    build()
                }

                ivCoverAnime.load(anime.coverImages) {
                    transformations(RoundedCornersTransformation(15f))
                    build()
                }

                tvAnimeTitle.text = anime.title
                if (anime.titleJapanese.isNullOrEmpty()) tvAnimeTitleJapanese.visibility = View.GONE
                else tvAnimeTitleJapanese.text = anime.titleJapanese

                tvRankAndRating.text = getString(
                    R.string.rank_and_score, anime.rank.toString(), anime.score ?: UNKNOWN
                )/*End of Header Section*/

                /*Body Section*/
                tvTypeValue.text = getString(R.string.information_value, anime.type)
                tvRatingValue.text =
                    if (anime.rating.isEmpty()) getString(R.string.information_value, UNKNOWN)
                    else getString(R.string.information_value, anime.rating)

                tvEpisodeValue.text = if (anime.episodes != null) getString(
                    R.string.information_value,
                    anime.episodes.toString()
                )
                else getString(R.string.information_value, UNKNOWN)

                tvGenreValue.text = if (anime.genres.isEmpty()) UNKNOWN
                else getString(R.string.information_value, ListToString().invoke(anime.genres))

                tvStatusValue.text = getString(R.string.information_value, anime.status)
                tvAiredValue.text =
                    if (anime.season.isNullOrEmpty()) getString(R.string.information_value, UNKNOWN)
                    else getString(
                        R.string.information_value, "${
                            (anime.season as String).replaceFirstChar { firstChar ->
                                firstChar.uppercase()
                            }
                        } ${anime.year}"
                    )

                tvSynopsis.text = anime.synopsis
                    ?: getString(R.string.no_information_by_mal)/*End of Body Section*/

                /*Fab Section*/
                fabFavorite.setImageResource(
                    if (anime.isFavorite) R.drawable.ic_favorite
                    else R.drawable.ic_favorite_hollow
                )/*End of Fab Section*/
            }
        }
    }

    private fun FragmentDetailAnimeBinding.setCharacters() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            val characterAdapter = CharacterAdapter { characterId ->
                val action = DetailAnimeFragmentDirections.getCharacterDetail(characterId)
                findNavController().navigate(action)
            }

            rvCharacter.apply {
                adapter = characterAdapter
                layoutManager = LinearLayoutManager(
                    requireContext(), RecyclerView.HORIZONTAL, false
                )
                setHasFixedSize(true)
            }

            viewModel.getAnimeCharactersById(args.malID).catch { t ->
                Snackbar.make(
                    root, t.localizedMessage ?: "Something Went Wrong", Snackbar.LENGTH_LONG
                ).show()
            }.collect { characters ->
                if (characters.isEmpty()) {
                    tvHeaderCharacter.visibility = View.GONE
                    return@collect
                }
                characterAdapter.submitList(characters)
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.fabFavorite.id -> {
                viewModel.updateAnimeFavorite(args.malID)
            }
        }
    }
}