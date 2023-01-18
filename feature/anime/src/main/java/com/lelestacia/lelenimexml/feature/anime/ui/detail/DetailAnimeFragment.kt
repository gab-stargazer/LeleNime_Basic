package com.lelestacia.lelenimexml.feature.anime.ui.detail

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
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
import com.lelestacia.lelenimexml.core.common.DateParser
import com.lelestacia.lelenimexml.core.common.R.anim.fade_in
import com.lelestacia.lelenimexml.core.common.R.anim.fade_out
import com.lelestacia.lelenimexml.feature.anime.R
import com.lelestacia.lelenimexml.feature.anime.databinding.FragmentDetailAnimeBinding
import com.lelestacia.lelenimexml.feature.anime.ui.adapter.CharacterAdapter
import com.lelestacia.lelenimexml.feature.anime.util.ListToString
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.transformers.coil.BlurTransformation
import kotlinx.coroutines.flow.catch

@AndroidEntryPoint
class DetailAnimeFragment : Fragment(R.layout.fragment_detail_anime), View.OnClickListener,
    View.OnScrollChangeListener {

    private val viewModel by viewModels<DetailAnimeViewModel>()
    private val args by navArgs<DetailAnimeFragmentArgs>()
    private val binding: FragmentDetailAnimeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            setView()
            setCharacters()
            fabFavorite.setOnClickListener(this@DetailAnimeFragment)
            scrollContent.setOnScrollChangeListener(this@DetailAnimeFragment)
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
                if (anime.titleJapanese.isNullOrEmpty() || anime.titleJapanese == anime.title || anime.titleJapanese == anime.titleEnglish)
                    tvAnimeTitleJapanese.visibility = View.GONE
                else tvAnimeTitleJapanese.text = anime.titleJapanese

                tvRankAndRating.text = getString(
                    R.string.rank_and_score, anime.rank.toString(), anime.score ?: UNKNOWN
                )
                tvUserRated.text = getString(R.string.rated_by, anime.scoredBy ?: 0)
                /*End of Header Section*/

                /*Body Section*/
                tvTypeValue.text = getText(anime.type)
                tvRatingValue.text = getText(anime.rating)

                tvEpisodeValue.text = if (anime.episodes != null) getText(anime.episodes.toString())
                else getText(null)

                tvGenreValue.text = if (anime.genres.isEmpty()) getText(UNKNOWN)
                else getText(ListToString().invoke(anime.genres))

                tvStatusValue.text = getText(anime.status)

                tvSeasonValue.text =
                    if (anime.season.isNullOrEmpty()) getText(null)
                    else getText(
                        "${
                            (anime.season as String).replaceFirstChar { firstChar ->
                                firstChar.uppercase()
                            }
                        } ${anime.year}"
                    )

                val parser = DateParser()
                tvAiredValue.text =
                    getText("${parser(anime.startedDate)} - ${parser(anime.finishedDate)}")

                tvSynopsis.text = anime.synopsis
                    ?: getString(R.string.no_information_by_mal)
                /*End of Body Section*/

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

    private fun getText(input: String?): String {
        if (input.isNullOrEmpty()) return getString(R.string.information_value, UNKNOWN)
        return getString(R.string.information_value, input)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.fabFavorite.id -> {
                viewModel.updateAnimeFavorite(args.malID)
            }
        }
    }

    override fun onScrollChange(
        v: View?,
        scrollX: Int,
        scrollY: Int,
        oldScrollX: Int,
        oldScrollY: Int
    ) {
        when (v?.id) {
            binding.scrollContent.id -> {
                if (scrollY == 0 && binding.fabFavorite.visibility == View.GONE) {
                    binding.fabFavorite.apply {
                        startAnimation(
                            AnimationUtils.loadAnimation(
                                requireContext(),
                                fade_in
                            )
                        )
                        visibility = View.VISIBLE
                    }
                } else if (scrollY > oldScrollY && binding.fabFavorite.visibility == View.VISIBLE) {
                    binding.fabFavorite.apply {
                        startAnimation(
                            AnimationUtils.loadAnimation(
                                requireContext(),
                                fade_out
                            )
                        )
                        visibility = View.GONE
                    }
                } else if (scrollY < oldScrollY && binding.fabFavorite.visibility == View.GONE) {
                    binding.fabFavorite.apply {
                        startAnimation(
                            AnimationUtils.loadAnimation(
                                requireContext(),
                                fade_in
                            )
                        )
                        visibility = View.VISIBLE
                    }
                }
            }
        }
    }
}
