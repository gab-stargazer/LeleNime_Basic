package com.lelestacia.lelenimexml.feature.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.lelestacia.lelenimexml.core.model.anime.Anime
import com.lelestacia.lelenimexml.feature.common.R
import com.lelestacia.lelenimexml.feature.common.databinding.ItemListAnimeBinding

class ListAnimePagingAdapterExtended(
    val onItemClicked: (Anime) -> Unit,
    val onItemLongClicked: (Anime) -> Unit
) : PagingDataAdapter<Anime, ListAnimePagingAdapterExtended.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ItemListAnimeBinding) :
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
                root.setOnClickListener {
                    onItemClicked(item)
                }
                root.setOnLongClickListener {
                    onItemLongClicked(item)
                    true
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListAnimeBinding.inflate(
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
