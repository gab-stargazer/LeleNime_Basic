package com.lelestacia.lelenimexml.feature.common.adapter.manga

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale
import coil.transform.RoundedCornersTransformation
import com.lelestacia.lelenimexml.core.model.manga.Manga
import com.lelestacia.lelenimexml.feature.common.R
import com.lelestacia.lelenimexml.feature.common.databinding.ItemListMangaBinding
import com.lelestacia.lelenimexml.feature.common.util.ModelDiffer.MANGA_DIFFING_UTIL
import kotlinx.coroutines.Dispatchers

class ItemListMangaPagingAdapter(
    private val onMangaClicked: (Manga) -> Unit
) : PagingDataAdapter<Manga, ItemListMangaPagingAdapter.ViewHolder>(MANGA_DIFFING_UTIL) {

    inner class ViewHolder(private val binding: ItemListMangaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Manga) {
            val context = binding.root.context
            val imageLoader = context.imageLoader
            val imageRequest: ImageRequest =
                ImageRequest.Builder(context)
                    .data(data = item.images)
                    .scale(Scale.FIT)
                    .placeholder(drawableResId = R.drawable.placeholder)
                    .crossfade(enable = true)
                    .fetcherDispatcher(dispatcher = Dispatchers.IO)
                    .transformationDispatcher(dispatcher = Dispatchers.Main)
                    .transformations(RoundedCornersTransformation(15f))
                    .networkCachePolicy(policy = CachePolicy.ENABLED)
                    .diskCachePolicy(policy = CachePolicy.ENABLED)
                    .diskCacheKey(key = "img-cache-${item.malID}")
                    .target(imageView = binding.ivCoverManga)
                    .build()
            imageLoader.enqueue(request = imageRequest)
            binding.tvTitleManga.text = item.title
            binding.tvScoreValue.text =
                context.getString(R.string.information_value, item.score.toString())
            binding.tvChapterValue.text =
                context.getString(R.string.information_value, item.chapters.toString())
            binding.tvStatusValue.text =
                context.getString(R.string.information_value, item.status)
            binding.root.setOnClickListener {
                onMangaClicked(item)
            }
        }
    }

    override fun onBindViewHolder(holder: ItemListMangaPagingAdapter.ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemListMangaPagingAdapter.ViewHolder {
        return ViewHolder(
            binding = ItemListMangaBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}