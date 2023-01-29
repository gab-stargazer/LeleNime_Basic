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
        setHeaderAndBody()
        setEpisodes()
        setCharacters()
        binding.apply {
            fabFavorite.setOnClickListener(this@DetailAnimeFragment)
            scrollContent.setOnScrollChangeListener(this@DetailAnimeFragment)
        }
    }

    private fun setHeaderAndBody() {
        val headerSection = binding.headerSection
        val bodySection = binding.bodySection
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.getAnimeByAnimeID(animeID = args.malID).collect { anime ->

                //  Header Section
                headerSection.apply {
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

                //  Body Section
                bodySection.apply {
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
                        if ((anime.episodes ?: 0) <= 2) {
                            getText(parser(anime.startedDate))
                        } else {
                            getText("${parser(anime.startedDate)} - ${parser(anime.finishedDate)}")
                        }


                    tvSynopsis.text = anime.synopsis
                        ?: getString(R.string.no_information_by_mal)
                }

                val isFavorite: Boolean = anime.isFavorite
                binding.fabFavorite.setImageResource(
                    if (isFavorite) R.drawable.ic_favorite
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
                            binding.root,
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
                        TransitionManager.endTransitions(binding.root)
                    }
                    is Resource.Success -> {
                        val episodes = result.data ?: emptyList()
                        if (episodes.isEmpty()) {
                            TransitionManager.beginDelayedTransition(binding.root)
                            episodeSection.root.visibility = View.GONE
                            TransitionManager.endTransitions(binding.root)
                        } else {
                            TransitionManager.beginDelayedTransition(
                                binding.root,
                                AutoTransition()
                            )
                            episodeAdapter.submitList(result.data)
                            episodeSection.rvEpisode.visibility = View.VISIBLE
                            episodeSection.rvEpisodeLoading.root.visibility = View.GONE
                            TransitionManager.endTransitions(binding.root)
                        }
                    }
                    Resource.Loading -> {
                        val isBtnRetryVisibile = episodeSection.btnRetry.visibility == View.VISIBLE
                        if (isBtnRetryVisibile) {
                            TransitionManager.beginDelayedTransition(
                                binding.root,
                                AutoTransition()
                            )
                            episodeSection.tvErrorMessage.visibility = View.GONE
                            episodeSection.btnRetry.visibility = View.GONE
                            episodeSection.rvEpisodeLoading.root.visibility = View.VISIBLE
                            TransitionManager.endTransitions(binding.root)
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

        val characterSection = binding.characterSection
        characterSection.rvCharacter.apply {
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
                val characterPlaceholder = characterSection.characterPlaceholder.root
                when (result) {
                    is Resource.Error -> {
                        characterPlaceholder.visibility = View.GONE
                        characterSection.rvCharacter.visibility = View.GONE
                        characterSection.tvErrorMessage.apply {
                            text = result.message
                            visibility = View.VISIBLE
                        }
                        characterSection.btnRetry.apply {
                            visibility = View.VISIBLE
                            setOnClickListener {
                                viewModel.getAnimeCharactersByAnimeID(animeID = args.malID)
                            }
                        }
                    }
                    is Resource.Success -> {
                        val characters = result.data ?: emptyList()
                        if (characters.isEmpty()) {
                            TransitionManager.beginDelayedTransition(binding.root)
                            characterSection.root.visibility = View.GONE
                            TransitionManager.endTransitions(binding.root)
                            return@collect
                        }

                        TransitionManager.beginDelayedTransition(binding.root)
                        characterAdapter.submitList(result.data)
                        characterSection.rvCharacter.visibility = View.VISIBLE
                        characterPlaceholder.visibility = View.GONE
                        TransitionManager.endTransitions(binding.root)
                    }
                    is Resource.Loading -> {
                        val isBtnRetryVisible = characterSection.btnRetry.visibility == View.VISIBLE
                        if (isBtnRetryVisible) {
                            characterPlaceholder.visibility = View.VISIBLE
                            characterSection.tvHeaderCharacter.visibility = View.VISIBLE
                            characterSection.rvCharacter.visibility = View.VISIBLE
                            characterSection.btnRetry.visibility = View.GONE
                            characterSection.tvErrorMessage.visibility = View.GONE
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
