package com.lelestacia.lelenimexml.feature.explore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale
import com.lelestacia.lelenimexml.core.model.anime.Anime
import com.lelestacia.lelenimexml.feature.common.R
import com.lelestacia.lelenimexml.feature.explore.databinding.ItemCardAnimeHorizontalBinding
import kotlinx.coroutines.Dispatchers

class HorizontalAnimePagingAdapter(
    private val onItemSelected: (Anime) -> Unit
) : PagingDataAdapter<Anime, HorizontalAnimePagingAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(
        private val binding: ItemCardAnimeHorizontalBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Anime) {
            binding.apply {
                val context = root.context
                val imageRequest: ImageRequest =
                    ImageRequest.Builder(context = context)
                        .data(data = item.coverImages)
                        .scale(Scale.FIT)
                        .placeholder(drawableResId = R.drawable.placeholder)
                        .crossfade(enable = true)
                        .diskCachePolicy(CachePolicy.ENABLED)
                        .fetcherDispatcher(Dispatchers.IO)
                        .transformationDispatcher(Dispatchers.Main)
                        .diskCacheKey("img-cache-${item.malID}")
                        .target(imageView = ivCoverAnime)
                        .build()
                context.imageLoader.enqueue(request = imageRequest)

                tvTitleAnime.text = item.title
                root.setOnClickListener {
                    onItemSelected(item)
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemCardAnimeHorizontalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Anime>() {
            override fun areItemsTheSame(oldItem: Anime, newItem: Anime): Boolean =
                oldItem == newItem

            override fun areContentsTheSame(oldItem: Anime, newItem: Anime): Boolean =
                oldItem.malID == newItem.malID
        }
    }
}