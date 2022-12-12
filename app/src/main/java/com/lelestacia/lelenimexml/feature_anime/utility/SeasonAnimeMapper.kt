package com.lelestacia.lelenimexml.feature_anime.utility

import com.lelestacia.lelenimexml.core.local.model.SeasonAnimeEntity
import com.lelestacia.lelenimexml.core.network.dto.anime.AnimeResponse
import com.lelestacia.lelenimexml.feature_anime.domain.model.Anime

object SeasonAnimeMapper {

    fun networkToAnime(networkData: AnimeResponse): Anime {
        return Anime(
            malId = networkData.malId,
            images = networkData.images.webp.largeImageUrl,
            trailer =
            if (networkData.trailer == null) null
            else Anime.Trailer(
                youtubeId = networkData.trailer.youtubeId,
                url = networkData.trailer.url,
                images = networkData.trailer.images?.largeImageUrl
            ),
            title = networkData.title,
            titleEnglish = networkData.titleEnglish,
            titleJapanese = networkData.titleJapanese,
            type = networkData.type,
            episodes = networkData.episodes,
            status = networkData.status,
            rating = networkData.rating,
            score = networkData.score,
            scoredBy = networkData.scoredBy,
            rank = networkData.rank,
            synopsis = networkData.synopsis,
            season = networkData.season,
            year = networkData.year,
            genres = networkData.genres.map {
                it.name
            }
        )
    }

    fun networkToEntities(networkData: AnimeResponse): SeasonAnimeEntity {
        return SeasonAnimeEntity(
            malId = networkData.malId,
            coverImages = networkData.images.webp.largeImageUrl,
            trailer = Anime.Trailer(
                youtubeId = networkData.trailer?.youtubeId,
                url = networkData.trailer?.url,
                images = networkData.trailer?.images?.largeImageUrl
            ),
            title =networkData.title,
            titleEnglish = networkData.titleEnglish,
            titleJapanese = networkData.titleJapanese,
            type = networkData.type,
            episodes = networkData.episodes,
            status = networkData.status,
            rating = networkData.rating,
            score = networkData.score,
            scoredBy =  networkData.scoredBy,
            rank = networkData.rank,
            synopsis = networkData.synopsis,
            season = networkData.season,
            year = networkData.year,
            genres = networkData.genres.map {
                it.name
            }
        )
    }

    fun entitiesToAnime(animeEntity: SeasonAnimeEntity) : Anime {
        return Anime(
            malId = animeEntity.malId,
            images = animeEntity.coverImages,
            trailer = animeEntity.trailer,
            title = animeEntity.title,
            titleEnglish = animeEntity.titleEnglish,
            titleJapanese = animeEntity.titleJapanese,
            type = animeEntity.type,
            episodes = animeEntity.episodes,
            status = animeEntity.status,
            rating = animeEntity.rating,
            score = animeEntity.score,
            scoredBy = animeEntity.scoredBy,
            rank = animeEntity.rank,
            synopsis = animeEntity.synopsis,
            season = animeEntity.season,
            year = animeEntity.year,
            genres = animeEntity.genres
        )
    }
}