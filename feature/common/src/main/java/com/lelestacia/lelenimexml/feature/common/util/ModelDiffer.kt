package com.lelestacia.lelenimexml.feature.common.util

import androidx.recyclerview.widget.DiffUtil
import com.lelestacia.lelenimexml.core.model.anime.Anime

object ModelDiffer {

    val ANIME_DIFFING_UTIL = object : DiffUtil.ItemCallback<Anime>() {
        override fun areItemsTheSame(oldItem: Anime, newItem: Anime): Boolean =
            oldItem.malID == newItem.malID

        override fun areContentsTheSame(oldItem: Anime, newItem: Anime): Boolean =
            oldItem == newItem
    }
}