package com.lelestacia.lelenimexml.feature_anime.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.lelestacia.lelenimexml.feature_anime.R
import com.lelestacia.lelenimexml.feature_anime.databinding.CardAnimeRowBinding
import com.lelestacia.lelenimexml.feature_anime.domain.model.Anime

class HistoryAnimeAdapter(
    val onAnimeSelected: (Anime) -> Unit
) : ListAdapter<Anime, HistoryAnimeAdapter.ViewHolder>(DIFF_CALLBACK) {


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
                    onAnimeSelected(item)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CardAnimeRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {  anime ->
            holder.bind(anime)
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