package com.lelestacia.lelenimexml.feature_anime.ui.detail_anime

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lelestacia.lelenimexml.core.model.local.AnimeEntity
import com.lelestacia.lelenimexml.feature_anime.domain.model.Anime
import com.lelestacia.lelenimexml.feature_anime.domain.usecases.AnimeUseCases
import com.lelestacia.lelenimexml.feature_anime.domain.usecases.CharacterUseCases
import com.lelestacia.lelenimexml.feature_anime.domain.utility.AnimeMapperUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailAnimeViewModel @Inject constructor(
    private val characterUseCases: CharacterUseCases,
    private val animeUseCases: AnimeUseCases
) : ViewModel() {
    fun getAnimeCharactersById(animeId: Int) = characterUseCases.getAnimeCharacterById(animeId)

    suspend fun getAnimeById(animeId: Int): Flow<AnimeEntity?> =
        animeUseCases.getNewestAnimeDataByAnimeId(animeId)

    fun insertNewOrUpdateLastViewed(anime: Anime) {
        viewModelScope.launch {
            val localAnime = animeUseCases.getAnimeByAnimeId(anime.malId)
            val newData = AnimeMapperUtil.animeToEntity(
                anime = anime,
                isFavorite = !(localAnime?.isFavorite ?: false)
            )
            animeUseCases.updateAnime(newData)
        }
    }
}