package com.lelestacia.lelenimexml.core.domain.di

import com.lelestacia.lelenimexml.core.data.impl.anime.IAnimeRepository
import com.lelestacia.lelenimexml.core.data.impl.character.ICharacterRepository
import com.lelestacia.lelenimexml.core.data.impl.episode.IEpisodeRepository
import com.lelestacia.lelenimexml.core.data.impl.recommendation.IRecommendationRepository
import com.lelestacia.lelenimexml.core.domain.usecase.anime.AnimeUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.anime.IAnimeUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.character.CharacterUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.character.ICharacterUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.episode.EpisodeUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.episode.IEpisodeUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.explore.ExploreUseCases
import com.lelestacia.lelenimexml.core.domain.usecase.explore.IExploreUseCases
import com.lelestacia.lelenimexml.core.domain.usecase.favorite.FavoriteAnimeUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.favorite.IFavoriteAnimeUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.history.HistoryAnimeUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.history.IHistoryAnimeUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.home.HomeAnimeUseCase
import com.lelestacia.lelenimexml.core.domain.usecase.home.IHomeAnimeUseCase
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

    @Provides
    @ViewModelScoped
    fun provideHomeAnimeUseCase(repository: IAnimeRepository): IHomeAnimeUseCase =
        HomeAnimeUseCase(animeRepository = repository)

    @Provides
    @ViewModelScoped
    fun provideHistoryAnimeUseCase(repository: IAnimeRepository): IHistoryAnimeUseCase =
        HistoryAnimeUseCase(animeRepository = repository)

    @Provides
    @ViewModelScoped
    fun provideFavoriteAnimeUseCase(repository: IAnimeRepository): IFavoriteAnimeUseCase =
        FavoriteAnimeUseCase(animeRepository = repository)

    @Provides
    @ViewModelScoped
    fun provideExploreUseCases(
        animeRepository: IAnimeRepository,
        recommendationRepository: IRecommendationRepository
    ): IExploreUseCases =
        ExploreUseCases(
            animeRepository = animeRepository,
            recommendationRepository = recommendationRepository
        )
}
