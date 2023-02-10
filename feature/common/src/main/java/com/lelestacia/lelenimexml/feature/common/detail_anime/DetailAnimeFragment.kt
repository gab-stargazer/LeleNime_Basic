package com.lelestacia.lelenimexml.feature.common.detail_anime

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.view.animation.AnimationUtils
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
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
import com.lelestacia.lelenimexml.core.model.episode.Episode
import com.lelestacia.lelenimexml.feature.common.R
import com.lelestacia.lelenimexml.feature.common.adapter.EpisodeAdapter
import com.lelestacia.lelenimexml.feature.common.databinding.DetailAnimeBodyBinding
import com.lelestacia.lelenimexml.feature.common.databinding.DetailAnimeHeaderBinding
import com.lelestacia.lelenimexml.feature.common.databinding.FragmentDetailAnimeBinding
import com.lelestacia.lelenimexml.feature.common.util.ListToString
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.transformers.coil.BlurTransformation
import kotlinx.coroutines.delay

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
        viewModel.updateAnimeIfNecessary(animeID = animeID)
        setHeaderAndBody()

        setCharacters()
        binding.apply {
            fabFavorite.setOnClickListener(this@DetailAnimeFragment)
            scrollContent.setOnScrollChangeListener(this@DetailAnimeFragment)
        }
    }

    private fun setHeaderAndBody() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        val headerSection: DetailAnimeHeaderBinding = binding.headerSection
        val bodySection: DetailAnimeBodyBinding = binding.bodySection
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
                val episodes = anime.episodes ?: 0
                tvAiredValue.text =
                    if (episodes <= 2) {
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

    private fun setEpisodes() = viewLifecycleOwner.lifecycleScope.launchWhenCreated {
        val episodeAdapter = EpisodeAdapter()
        val episodeSection = binding.episodeSection
        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(episodeSection.rvEpisode)
        episodeSection.rvEpisode.apply {
            adapter = episodeAdapter
            layoutManager = LinearLayoutManager(
                requireContext(),
                RecyclerView.HORIZONTAL,
                false
            )
        }

        viewModel.episodes.collect { result: Resource<List<Episode>> ->
            when (result) {
                is Resource.Error -> {
                    val isDataNull = result.data.isNullOrEmpty()
                    if (isDataNull) {
                        TransitionManager.beginDelayedTransition(binding.root)
                        episodeSection.rvEpisodeLoading.root.visibility = View.INVISIBLE
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
                        return@collect
                    }

                    episodeAdapter.submitList(result.data)
                    val isPlaceholderPresent =
                        episodeSection.rvEpisodeLoading.root.visibility == View.VISIBLE
                    TransitionManager.beginDelayedTransition(binding.root)
                    episodeSection.rvEpisode.visibility = View.VISIBLE
                    if (isPlaceholderPresent) {
                        episodeSection.rvEpisodeLoading.root.visibility = View.GONE
                    }
                    episodeSection.progressUpdate.visibility = View.INVISIBLE
                    episodeSection.tvErrorBottom.apply {
                        text = result.message
                        visibility = View.VISIBLE
                    }
                    delay(5000)
                    TransitionManager.beginDelayedTransition(binding.root)
                    episodeSection.tvErrorBottom.visibility = View.GONE
                }
                is Resource.Success -> {
                    val isPlaceholderPresent =
                        episodeSection.rvEpisodeLoading.root.visibility == View.VISIBLE
                    val episodes = result.data ?: emptyList()
                    if (isPlaceholderPresent && episodes.isEmpty()) {
                        TransitionManager.beginDelayedTransition(binding.root)
                        episodeSection.root.visibility = View.GONE
                        return@collect
                    }

                    if (isPlaceholderPresent) {
                        TransitionManager.beginDelayedTransition(
                            episodeSection.rvEpisodeLoading.root,
                            AutoTransition()
                        )
                        episodeAdapter.submitList(episodes)
                        episodeSection.rvEpisode.visibility = View.VISIBLE
                        episodeSection.rvEpisodeLoading.root.visibility = View.GONE
                        return@collect
                    }

                    TransitionManager.beginDelayedTransition(binding.root)
                    episodeSection.progressUpdate.visibility = View.GONE
                    episodeSection.tvErrorBottom.apply {
                        text = getString(R.string.episodes_updated)
                        visibility = View.VISIBLE
                    }
                    delay(5000)
                    TransitionManager.beginDelayedTransition(binding.root)
                    episodeSection.tvErrorBottom.visibility = View.GONE
                }
                is Resource.Loading -> {
                    val isEpisodesExist = episodeAdapter.itemCount > 0
                    if (isEpisodesExist) {
                        TransitionManager.beginDelayedTransition(
                            episodeSection.root,
                            AutoTransition().excludeTarget(
                                episodeSection.rvEpisodeLoading.root,
                                true
                            )
                        )
                        episodeSection.progressUpdate.visibility = View.VISIBLE
                        return@collect
                    }

                    TransitionManager.beginDelayedTransition(episodeSection.root)
                    episodeSection.rvEpisodeLoading.root.visibility = View.VISIBLE
                    episodeSection.btnRetry.visibility = View.GONE
                    episodeSection.tvErrorMessage.visibility = View.GONE
                }
                else -> Unit
            }
        }
    }

    private fun setCharacters(){}

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
