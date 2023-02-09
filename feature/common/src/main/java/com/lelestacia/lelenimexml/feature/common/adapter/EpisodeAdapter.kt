package com.lelestacia.lelenimexml.feature.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lelestacia.lelenimexml.core.common.DateParser
import com.lelestacia.lelenimexml.core.model.episode.Episode
import com.lelestacia.lelenimexml.feature.common.databinding.ItemCardEpisodeBinding

class EpisodeAdapter : ListAdapter<Episode, EpisodeAdapter.ViewHolder>(DIFF_CALLBACK) {

    private val dateParser: DateParser = DateParser()

    inner class ViewHolder(private val binding: ItemCardEpisodeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Episode) {
            binding.apply {
                tvTitleEpisode.text = item.title
                tvScoreEpisode.text = item.score.toString()
                tvAired.text = dateParser(item.aired)

                val episodeNumber = "#" + item.malID
                tvEpisodeNumber.text = episodeNumber

                val isJapaneseTitleEmpty = item.titleJapanese.isNullOrEmpty()
                if (isJapaneseTitleEmpty) {
                    tvTitleJapaneseEpisode.visibility = View.GONE
                } else {
                    tvTitleJapaneseEpisode.text = item.titleJapanese
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCardEpisodeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { episode ->
            holder.bind(episode)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Episode>() {
            override fun areItemsTheSame(oldItem: Episode, newItem: Episode): Boolean =
                oldItem.malID == newItem.malID

            override fun areContentsTheSame(oldItem: Episode, newItem: Episode): Boolean =
                oldItem == newItem
        }
    }
}
