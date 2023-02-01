package com.lelestacia.lelenimexml.core.domain.usecase.history

import androidx.paging.PagingData
import com.lelestacia.lelenimexml.core.data.impl.anime.IAnimeRepository
import com.lelestacia.lelenimexml.core.model.anime.Anime
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HistoryAnimeUseCase @Inject constructor(
    private val animeRepository: IAnimeRepository
) : IHistoryAnimeUseCase {
    override fun getAnimeHistory(): Flow<PagingData<Anime>> {
        return animeRepository.getAnimeHistory()
    }
}
