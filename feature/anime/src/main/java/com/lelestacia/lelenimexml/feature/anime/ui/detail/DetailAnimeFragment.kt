package com.lelestacia.lelenimexml.feature.anime.ui.detail

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.view.animation.AnimationUtils
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import coil.load
import coil.transform.RoundedCornersTransformation
import com.lelestacia.lelenimexml.core.common.Constant.UNKNOWN
import com.lelestacia.lelenimexml.core.common.DateParser
import com.lelestacia.lelenimexml.core.common.R.anim.fade_in
import com.lelestacia.lelenimexml.core.common.R.anim.fade_out
import com.lelestacia.lelenimexml.core.common.Resource
import com.lelestacia.lelenimexml.core.model.character.Character
import com.lelestacia.lelenimexml.core.model.episode.Episode
import com.lelestacia.lelenimexml.feature.anime.R
import com.lelestacia.lelenimexml.feature.anime.databinding.DetailAnimeEpisodesBinding
import com.lelestacia.lelenimexml.feature.anime.databinding.FragmentDetailAnimeBinding
import com.lelestacia.lelenimexml.feature.anime.ui.adapter.CharacterAdapter
import com.lelestacia.lelenimexml.feature.anime.ui.adapter.EpisodeAdapter
import com.lelestacia.lelenimexml.feature.anime.util.ListToString
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.transformers.coil.BlurTransformation

@AndroidEntryPoint
class DetailAnimeFragment :
    Fragment(R.layout.fragment_detail_anime),
    View.OnClickListener,
    View.OnScrollChangeListener {

    private val viewModel by viewModels<DetailAnimeViewModel>()
    private val args by navArgs<DetailAnimeFragmentArgs>()
    private val binding: FragmentDetailAnimeBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val animeID = args.malID
        viewModel.getEpisodesByAnimeID(animeID = animeID)
        viewModel.getAnimeCharactersByAnimeID(animeID = animeID)
        setView()
        setEpisodes()
        setCharacters()
        binding.apply {
            fabFavorite.setOnClickListener(this@DetailAnimeFragment)
            scrollContent.setOnScrollChangeListener(this@DetailAnimeFragment)
        }
    }

    private fun setView() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.getAnimeByAnimeID(args.malID).collect { anime ->
                binding.headerSection.apply {
                    ivBackgroundAnime.load(anime.coverImages) {
                        transformations(BlurTransformation(requireContext()))
                        build()
                    }
                    ivCoverAnime.load(anime.coverImages) {
                        transformations(RoundedCornersTransformation(15f))
                        build()
                    }

                    tvAnimeTitle.text = anime.title
                    tvAnimeTitleJapanese.text = anime.titleJapanese
                    if (anime.titleJapanese.isNullOrEmpty() || anime.titleJapanese == anime.title || anime.titleJapanese == anime.titleEnglish)
                        tvAnimeTitleJapanese.visibility = View.GONE
                    else tvAnimeTitleJapanese.text = anime.titleJapanese

                    tvRankAndRating.text = getString(
                        R.string.rank_and_score, anime.rank.toString(), anime.score ?: UNKNOWN
                    )
                    tvUserRated.text = getString(R.string.rated_by, anime.scoredBy ?: 0)
                }

                binding.bodySection.apply {
                    tvTypeValue.text = getText(anime.type)
                    tvRatingValue.text = getText(anime.rating)

                    tvEpisodeValue.text =
                        if (anime.episodes != null) getText(anime.episodes.toString())
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
                }

                binding.fabFavorite.setImageResource(
                    if (anime.isFavorite) R.drawable.ic_favorite
                    else R.drawable.ic_favorite_hollow
                )
            }
        }
    }

    private fun setEpisodes() {
        val episodeAdapter = EpisodeAdapter()
        val episodeSection: DetailAnimeEpisodesBinding = binding.episodeSection
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(binding.episodeSection.rvEpisode)
        binding.episodeSection.rvEpisode.apply {
            adapter = episodeAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.HORIZONTAL,
                false
            )
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.episodes.collect { result: Resource<List<Episode>> ->
                when (result) {
                    is Resource.Error -> {
                        TransitionManager.beginDelayedTransition(
                            episodeSection.root,
                            AutoTransition()
                        )
                        episodeSection.rvEpisodeLoading.root.visibility = View.GONE
                        episodeSection.tvErrorMessage.apply {
                            text = result.message
                            visibility = View.VISIBLE
                        }
                        episodeSection.btnRetry.apply {
                            visibility = View.VISIBLE
                            setOnClickListener {
                                viewModel.getEpisodesByAnimeID(animeID = args.malID)
                            }
                        }
                        TransitionManager.endTransitions(episodeSection.root)
                    }
                    is Resource.Success -> {
                        val episodes = result.data ?: emptyList()
                        if (episodes.isEmpty()) {
                            TransitionManager.beginDelayedTransition(episodeSection.root)
                            episodeSection.root.visibility = View.GONE
                            TransitionManager.endTransitions(episodeSection.root)
                        } else {
                            TransitionManager.beginDelayedTransition(
                                episodeSection.root,
                                AutoTransition()
                            )
                            episodeAdapter.submitList(result.data)
                            episodeSection.rvEpisode.visibility = View.VISIBLE
                            episodeSection.rvEpisodeLoading.root.visibility = View.GONE
                            TransitionManager.endTransitions(episodeSection.root)
                        }
                    }
                    Resource.Loading -> {
                        val isBtnRetryVisibile = episodeSection.btnRetry.visibility == View.VISIBLE
                        if (isBtnRetryVisibile) {
                            TransitionManager.beginDelayedTransition(
                                episodeSection.root,
                                AutoTransition())
                            episodeSection.tvErrorMessage.visibility = View.GONE
                            episodeSection.btnRetry.visibility = View.GONE
                            episodeSection.rvEpisodeLoading.root.visibility = View.VISIBLE
                            TransitionManager.endTransitions(episodeSection.root)
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun setCharacters() {
        val characterAdapter = CharacterAdapter { characterId ->
            val action = DetailAnimeFragmentDirections.getCharacterDetail(characterId)
            findNavController().navigate(action)
        }

        binding.characterSection.rvCharacter.apply {
            adapter = characterAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.HORIZONTAL,
                false
            )
            setHasFixedSize(true)
        }

        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.characters.collect { result: Resource<List<Character>> ->
                when (result) {
                    is Resource.Error -> {
                        binding.characterSection.apply {
                            progressCircular.visibility = View.GONE
                            tvHeaderCharacter.visibility = View.GONE
                            rvCharacter.visibility = View.GONE
                            tvErrorMessage.text = result.message
                            tvErrorMessage.visibility = View.VISIBLE
                            btnRetry.visibility = View.VISIBLE
                            btnRetry.setOnClickListener {
                                viewModel.getAnimeCharactersByAnimeID(animeID = args.malID)
                            }
                        }
                    }
                    is Resource.Success -> {
                        characterAdapter.submitList(result.data)
                        binding.characterSection.progressCircular.visibility = View.GONE
                    }
                    is Resource.Loading -> {
                        binding.characterSection.apply {
                            val isBtnRetryVisible = btnRetry.visibility == View.VISIBLE
                            if (isBtnRetryVisible) {
                                progressCircular.visibility = View.VISIBLE
                                tvHeaderCharacter.visibility = View.VISIBLE
                                rvCharacter.visibility = View.VISIBLE
                                btnRetry.visibility = View.GONE
                                tvErrorMessage.visibility = View.GONE
                            }
                        }
                    }
                    else -> Unit
                }
            }
        }
    }

    private fun getText(input: String?): String {
        return if (input.isNullOrEmpty()) getString(R.string.information_value, UNKNOWN)
        else getString(R.string.information_value, input)
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
