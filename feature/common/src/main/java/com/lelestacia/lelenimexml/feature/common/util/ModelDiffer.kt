package com.lelestacia.lelenimexml.feature.common.util

import androidx.recyclerview.widget.DiffUtil
import com.lelestacia.lelenimexml.core.model.anime.Anime
import com.lelestacia.lelenimexml.core.model.manga.Manga

object ModelDiffer {

    val ANIME_DIFFING_UTIL = object : DiffUtil.ItemCallback<Anime>() {
        override fun areItemsTheSame(oldItem: Anime, newItem: Anime): Boolean =
            oldItem.malID == newItem.malID

        override fun areContentsTheSame(oldItem: Anime, newItem: Anime): Boolean =
            oldItem == newItem
    }

    val MANGA_DIFFING_UTIL = object : DiffUtil.ItemCallback<Manga>() {
        override fun areItemsTheSame(oldItem: Manga, newItem: Manga): Boolean =
            oldItem.malID == newItem.malID

        override fun areContentsTheSame(oldItem: Manga, newItem: Manga): Boolean =
            oldItem == newItem
    }
}