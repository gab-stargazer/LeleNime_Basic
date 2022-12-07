package com.lelestacia.lelenimexml.feature_anime.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.lelestacia.lelenimexml.R
import com.lelestacia.lelenimexml.core.domain.dto.season.Data
import com.lelestacia.lelenimexml.databinding.CardAnimeRowBinding

class AnimeRowPagingAdapter :
    PagingDataAdapter<Data, AnimeRowPagingAdapter.ViewHolder>(DIFF_CALLBACk) {

    inner class ViewHolder(private val binding: CardAnimeRowBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Data) {
            binding.apply {
                val context = root.context
                ivCoverAnime.load(item.images.jpg.largeImageUrl) {
                    crossfade(true)
                }
                tvTitleAnime.text = item.title
                tvRatingAnime.text = context.getString(R.string.rating, item.rating)
                tvEpisodeAnime.text = context.getString(R.string.episode, item.episodes.toString())
                tvStatusAnime.text = context.getString(R.string.status, item.status)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            CardAnimeRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        private val DIFF_CALLBACk = object : DiffUtil.ItemCallback<Data>() {
            override fun areItemsTheSame(oldItem: Data, newItem: Data): Boolean =
                oldItem.malId == newItem.malId

            override fun areContentsTheSame(oldItem: Data, newItem: Data): Boolean =
                oldItem == newItem
        }
    }
}