package com.lelestacia.lelenimexml.core.domain.di

import com.lelestacia.lelenimexml.core.data.impl.anime.IAnimeRepository
import com.lelestacia.lelenimexml.core.data.impl.character.ICharacterRepository
import com.lelestacia.lelenimexml.core.data.impl.episode.IEpisodeRepository
import com.lelestacia.lelenimexml.core.domain.usecase.*
import com.lelestacia.lelenimexml.core.domain.usecase.anime.AnimeUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.anime.IAnimeUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.character.CharacterUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.character.ICharacterUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.episode.EpisodeUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.episode.IEpisodeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideAnimeUseCase(repository: IAnimeRepository): IAnimeUseCase =
        AnimeUseCase(animeRepository = repository)

    @Provides
    @ViewModelScoped
    fun provideCharacterUseCase(repository: ICharacterRepository): ICharacterUseCase =
        CharacterUseCase(characterRepository = repository)

    @Provides
    @ViewModelScoped
    fun provideEpisodeUseCase(repository: IEpisodeRepository): IEpisodeUseCase =
        EpisodeUseCase(episodeRepository = repository)
}
