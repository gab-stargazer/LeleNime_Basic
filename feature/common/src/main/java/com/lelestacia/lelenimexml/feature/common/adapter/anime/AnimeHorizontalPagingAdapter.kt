package com.lelestacia.lelenimexml.feature.common.adapter.anime

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.ImageLoader
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale
import com.lelestacia.lelenimexml.core.model.anime.Anime
import com.lelestacia.lelenimexml.feature.common.R
import com.lelestacia.lelenimexml.feature.common.databinding.AnimeHorizontalItemBinding
import com.lelestacia.lelenimexml.feature.common.util.ModelDiffer.ANIME_DIFFING_UTIL
import kotlinx.coroutines.Dispatchers

class AnimeHorizontalPagingAdapter(
    private val onAnimeClicked: (Anime) -> Unit
) : PagingDataAdapter<Anime, AnimeHorizontalPagingAdapter.ViewHolder>(ANIME_DIFFING_UTIL) {

    inner class ViewHolder(private val binding: AnimeHorizontalItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Anime) {
            val context: Context = binding.root.context
            val imageLoader: ImageLoader = context.imageLoader
            val imageRequest: ImageRequest =
                ImageRequest.Builder(context = context)
                    .data(data = item.coverImages)
                    .scale(Scale.FIT)
                    .placeholder(drawableResId = R.drawable.placeholder)
                    .crossfade(enable = true)
                    .fetcherDispatcher(dispatcher = Dispatchers.IO)
                    .transformationDispatcher(dispatcher = Dispatchers.Main)
                    .networkCachePolicy(policy = CachePolicy.ENABLED)
                    .diskCachePolicy(policy = CachePolicy.ENABLED)
                    .diskCacheKey(key = "img-cache-${item.malID}")
                    .target(imageView = binding.ivCoverAnime)
                    .build()
            imageLoader.enqueue(request = imageRequest)
            binding.tvTitleAnime.text = item.title
            binding.root.setOnClickListener {
                onAnimeClicked(item)
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AnimeHorizontalPagingAdapter.ViewHolder {
        return ViewHolder(
            binding = AnimeHorizontalItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: AnimeHorizontalPagingAdapter.ViewHolder, position: Int) {
        getItem(position = position)?.let { anime ->
            holder.bind(item = anime)
        }
    }
}