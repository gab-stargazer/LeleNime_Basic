package com.lelestacia.lelenimexml.feature_anime.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.lelestacia.lelenimexml.R
import com.lelestacia.lelenimexml.databinding.CardAnimeRowBinding
import com.lelestacia.lelenimexml.feature_anime.domain.model.AnimeCard

class AnimeRowPagingAdapter(val onClicked: (Int) -> Unit) :
    PagingDataAdapter<AnimeCard, AnimeRowPagingAdapter.ViewHolder>(DIFF_CALLBACk) {

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

        fun bind(item: AnimeCard) {
            binding.apply {
                val context = root.context
                ivCoverAnime.load(item.coverImage) {
                    crossfade(true)
                }
                tvTitleAnime.text = item.title
                tvRatingAnime.text = context.getString(R.string.rating, item.rating.toString())
                tvEpisodeAnime.text = context.getString(R.string.episode, item.episode.toString())
                tvStatusAnime.text = context.getString(R.string.status, item.status)

                root.setOnClickListener {
                    onClicked(item.malID)
                }
            }
        }
    }

    companion object {
        private val DIFF_CALLBACk = object : DiffUtil.ItemCallback<AnimeCard>() {
            override fun areItemsTheSame(oldItem: AnimeCard, newItem: AnimeCard): Boolean =
                oldItem.malID == newItem.malID

            override fun areContentsTheSame(oldItem: AnimeCard, newItem: AnimeCard): Boolean =
                oldItem == newItem
        }
    }
}