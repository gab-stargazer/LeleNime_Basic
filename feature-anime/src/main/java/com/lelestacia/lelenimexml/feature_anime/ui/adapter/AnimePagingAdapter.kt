package com.lelestacia.lelenimexml.feature_anime.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.lelestacia.lelenimexml.feature_anime.R
import com.lelestacia.lelenimexml.feature_anime.databinding.CardAnimeRowBinding
import com.lelestacia.lelenimexml.feature_anime.domain.model.Anime

class AnimePagingAdapter(val onClicked: (Anime) -> Unit) :
    PagingDataAdapter<Anime, AnimePagingAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CardAnimeRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        if (item != null)
            holder.bind(item)
    }

    inner class ViewHolder(private val binding: CardAnimeRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Anime) {
            binding.apply {
                val context = root.context
                ivCoverAnime.load(item.images) {
                    crossfade(true)
                }
                tvTitleAnime.text = item.title
                tvRatingAnime.text = context
                    .getString(R.string.rating, item.score ?: UNKNOWN)
                tvEpisodeAnime.text = context
                    .getString(R.string.episode, item.episodes ?: UNKNOWN)
                tvStatusAnime.text = context
                    .getString(R.string.status, item.status)
                root.setOnClickListener {
                    onClicked(item)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Anime>() {
            override fun areItemsTheSame(oldItem: Anime, newItem: Anime): Boolean =
                oldItem.malId == newItem.malId

            override fun areContentsTheSame(oldItem: Anime, newItem: Anime): Boolean =
                oldItem == newItem
        }
        private const val UNKNOWN = "Unknown"
    }
}