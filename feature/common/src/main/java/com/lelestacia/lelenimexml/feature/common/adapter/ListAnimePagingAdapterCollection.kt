package com.lelestacia.lelenimexml.feature.common.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.lelestacia.lelenimexml.core.model.anime.Anime
import com.lelestacia.lelenimexml.feature.common.R
import com.lelestacia.lelenimexml.feature.common.databinding.ItemListAnimeCollectionBinding

class ListAnimePagingAdapterCollection(val onItemClicked: (Anime) -> Unit) :
    PagingDataAdapter<Anime, ListAnimePagingAdapterCollection.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ItemListAnimeCollectionBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Anime) {
            binding.apply {
                val context = root.context
                ivCoverAnime.load(item.coverImages) {
                    transformations(RoundedCornersTransformation(15f))
                    build()
                }
                tvTitleAnime.text = item.title
                tvStatusAnime.text = item.status
                tvRatingAnime.text = context
                    .getString(R.string.rating_item_anime, item.score ?: "Unknown")
                tvEpisodeAnime.text = context
                    .getString(R.string.episode_item_anime, item.episodes ?: "Unknown")
                tvStatusAnime.text = context
                    .getString(R.string.status_item_anime, item.status)
                val favoriteIcon: Drawable? =
                    if (item.isFavorite) {
                        ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.ic_favorite,
                            context.theme
                        )
                    } else {
                        ResourcesCompat.getDrawable(
                            context.resources,
                            R.drawable.ic_favorite_hollow,
                            context.theme
                        )
                    }
                ivFavoriteIcon.setImageDrawable(favoriteIcon)
                root.setOnClickListener {
                    onItemClicked(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListAnimeCollectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { anime ->
            holder.bind(anime)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Anime>() {
            override fun areItemsTheSame(oldItem: Anime, newItem: Anime): Boolean =
                oldItem.malID == newItem.malID

            override fun areContentsTheSame(oldItem: Anime, newItem: Anime): Boolean =
                oldItem == newItem
        }
    }
}