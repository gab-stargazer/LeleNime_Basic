package com.lelestacia.lelenimexml.feature.explore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.lelestacia.lelenimexml.core.model.anime.Anime
import com.lelestacia.lelenimexml.feature.explore.databinding.ItemCardAnimeHorizontalBinding

class HorizontalAnimePagingAdapter(private val onItemSelected: (Anime) -> Unit) :
    PagingDataAdapter<Anime, HorizontalAnimePagingAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(
        private val binding: ItemCardAnimeHorizontalBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Anime) {
            binding.apply {
                ivCoverAnime.load(item.coverImages)
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