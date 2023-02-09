package com.lelestacia.lelenimexml.feature.common.detail_anime

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lelestacia.lelenimexml.core.common.Constant.SHORT_DELAY
import com.lelestacia.lelenimexml.core.common.Resource
import com.lelestacia.lelenimexml.core.domain.usecase.anime.IAnimeUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.character.ICharacterUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.episode.EpisodeUseCase
import com.lelestacia.lelenimexml.core.model.anime.Anime
import com.lelestacia.lelenimexml.core.model.character.Character
import com.lelestacia.lelenimexml.core.model.episode.Episode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailAnimeViewModel @Inject constructor(
    private val characterUseCase: ICharacterUseCase,
    private val episodeUseCase: EpisodeUseCase,
    private val animeUseCase: IAnimeUseCase
) : ViewModel() {

    private val _characters: MutableStateFlow<Resource<List<Character>>> =
        MutableStateFlow(Resource.None)
    val characters get() = _characters.asStateFlow()

    private val _episodes: MutableStateFlow<Resource<List<Episode>>> =
        MutableStateFlow(Resource.None)
    val episodes get() = _episodes.asStateFlow()

    private val _anime: MutableStateFlow<Resource<Anime>> = MutableStateFlow(Resource.None)
    val anime get() = _anime.asStateFlow()

    private val _updateStatus: MutableLiveData<Resource<Boolean>> = MutableLiveData(Resource.None)
    val updateStatus: LiveData<Resource<Boolean>> = _updateStatus

    fun getAnimeCharactersByAnimeID(animeID: Int) = viewModelScope.launch {
        characterUseCase.getAnimeCharacterById(animeID)
            .collect { result: Resource<List<Character>> ->
                delay(SHORT_DELAY)
                _characters.emit(result)
            }
    }

    fun getEpisodesByAnimeID(animeID: Int) = viewModelScope.launch {
        episodeUseCase.getEpisodesByAnimeID(animeID = animeID)
            .collect { result: Resource<List<Episode>> ->
                delay(SHORT_DELAY)
                _episodes.emit(result)
            }
    }

    fun getAnimeByAnimeID(animeID: Int): Flow<Anime> =
        animeUseCase.getAnimeByMalID(animeID)

    fun updateAnimeFavorite(animeID: Int) = viewModelScope.launch {
        animeUseCase.updateAnimeFavorite(animeID = animeID)
    }

    fun updateAnimeIfNecessary(animeID: Int) = viewModelScope.launch {
        _updateStatus.value = animeUseCase.updateAnimeIfNecessary(animeID = animeID)
    }
}
