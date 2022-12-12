package com.lelestacia.lelenimexml.core.model.remote.animefull


import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("aired")
    val aired: Aired = Aired(),
    @SerializedName("airing")
    val airing: Boolean = false,
    @SerializedName("approved")
    val approved: Boolean = false,
    @SerializedName("background")
    val background: String = "",
    @SerializedName("broadcast")
    val broadcast: Broadcast = Broadcast(),
    @SerializedName("demographics")
    val demographics: List<Demographic> = listOf(),
    @SerializedName("duration")
    val duration: String = "",
    @SerializedName("episodes")
    val episodes: Int = 0,
    @SerializedName("explicit_genres")
    val explicitGenres: List<Any> = listOf(),
    @SerializedName("external")
    val `external`: List<External> = listOf(),
    @SerializedName("favorites")
    val favorites: Int = 0,
    @SerializedName("genres")
    val genres: List<Genre> = listOf(),
    @SerializedName("images")
    val images: Images = Images(),
    @SerializedName("licensors")
    val licensors: List<Any> = listOf(),
    @SerializedName("mal_id")
    val malId: Int = 0,
    @SerializedName("members")
    val members: Int = 0,
    @SerializedName("popularity")
    val popularity: Int = 0,
    @SerializedName("producers")
    val producers: List<Any> = listOf(),
    @SerializedName("rank")
    val rank: Int = 0,
    @SerializedName("rating")
    val rating: String = "",
    @SerializedName("relations")
    val relations: List<Relation> = listOf(),
    @SerializedName("score")
    val score: Double = 0.0,
    @SerializedName("scored_by")
    val scoredBy: Int = 0,
    @SerializedName("season")
    val season: String = "",
    @SerializedName("source")
    val source: String = "",
    @SerializedName("status")
    val status: String = "",
    @SerializedName("streaming")
    val streaming: List<Streaming> = listOf(),
    @SerializedName("studios")
    val studios: List<Studio> = listOf(),
    @SerializedName("synopsis")
    val synopsis: String = "",
    @SerializedName("theme")
    val theme: Theme = Theme(),
    @SerializedName("themes")
    val themes: List<ThemeX> = listOf(),
    @SerializedName("title")
    val title: String = "",
    @SerializedName("title_english")
    val titleEnglish: String = "",
    @SerializedName("title_japanese")
    val titleJapanese: String = "",
    @SerializedName("title_synonyms")
    val titleSynonyms: List<Any> = listOf(),
    @SerializedName("titles")
    val titles: List<Title> = listOf(),
    @SerializedName("trailer")
    val trailer: Trailer = Trailer(),
    @SerializedName("type")
    val type: String = "",
    @SerializedName("url")
    val url: String = "",
    @SerializedName("year")
    val year: Int = 0
)