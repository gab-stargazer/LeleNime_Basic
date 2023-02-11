package com.lelestacia.lelenimexml.core.data.utility

import com.lelestacia.lelenimexml.core.database.entity.episode.EpisodeEntity
import com.lelestacia.lelenimexml.core.model.episode.Episode
import com.lelestacia.lelenimexml.core.network.model.episodes.EpisodeResponse
import java.util.Date

fun EpisodeResponse.asNewEntity(animeID: Int): EpisodeEntity = EpisodeEntity(
    episodeID = malID,
    animeID = animeID,
    title = title,
    titleJapanese = titleJapanese,
    aired = aired,
    score = score,
    filler = filler,
    recap = recap,
    forumURL = forumURL,
    createdAt = Date(),
    updatedAt = null,
    titleRomanji = titleRomanji
)

fun EpisodeEntity.asEpisode(): Episode = Episode(
    malID = episodeID,
    animeID = animeID,
    title = title,
    titleJapanese = titleJapanese,
    aired = aired,
    score = score,
    filler = filler,
    recap = recap,
    forumURL = forumURL,
    titleRomanji = titleRomanji
)
