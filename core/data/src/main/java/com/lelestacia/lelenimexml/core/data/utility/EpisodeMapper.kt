package com.lelestacia.lelenimexml.core.data.utility

import com.lelestacia.lelenimexml.core.database.model.episode.EpisodeEntity
import com.lelestacia.lelenimexml.core.model.episode.Episode
import com.lelestacia.lelenimexml.core.network.model.episodes.NetworkEpisode
import java.util.*

fun Episode.asNewEntity(): EpisodeEntity = EpisodeEntity(
    malID = malID,
    animeID = animeID,
    title = title,
    titleJapanese = titleJapanese,
    aired = aired,
    score = score,
    filler = filler,
    recap = recap,
    forumURL = forumURL,
    createdAt = Date(),
    updatedAt = null
)

fun Episode.asEntity(createdAt: Date): EpisodeEntity = EpisodeEntity(
    malID = malID,
    animeID = animeID,
    title = title,
    titleJapanese = titleJapanese,
    aired = aired,
    score = score,
    filler = filler,
    recap = recap,
    forumURL = forumURL,
    createdAt = createdAt,
    updatedAt = Date()
)

fun NetworkEpisode.asNewEntity(animeID: Int): EpisodeEntity = EpisodeEntity(
    malID = malID,
    animeID = animeID,
    title = title,
    titleJapanese = titleJapanese,
    aired = aired,
    score = score,
    filler = filler,
    recap = recap,
    forumURL = forumURL,
    createdAt = Date(),
    updatedAt = null
)

fun NetworkEpisode.asEpisode(animeID: Int): Episode = Episode(
    malID = malID,
    animeID = animeID,
    title = title,
    titleJapanese = titleJapanese,
    aired = aired,
    score = score,
    filler = filler,
    recap = recap,
    forumURL = forumURL
)

fun EpisodeEntity.asEpisode(): Episode = Episode(
    malID = malID,
    animeID = animeID,
    title = title,
    titleJapanese = titleJapanese,
    aired = aired,
    score = score,
    filler = filler,
    recap = recap,
    forumURL = forumURL
)
