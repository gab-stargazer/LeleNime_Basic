package com.lelestacia.lelenimexml.core.data

import com.lelestacia.lelenimexml.core.common.Resource
import com.lelestacia.lelenimexml.core.data.dummy.chainsawManCharacters
import com.lelestacia.lelenimexml.core.data.dummy.powerCharacterDetail
import com.lelestacia.lelenimexml.core.data.dummy.powerProfile
import com.lelestacia.lelenimexml.core.data.impl.character.CharacterRepository
import com.lelestacia.lelenimexml.core.data.impl.character.ICharacterRepository
import com.lelestacia.lelenimexml.core.data.utility.JikanErrorParserUtil
import com.lelestacia.lelenimexml.core.data.utility.asCharacterDetail
import com.lelestacia.lelenimexml.core.data.utility.asEntity
import com.lelestacia.lelenimexml.core.database.impl.character.ICharacterDatabaseService
import com.lelestacia.lelenimexml.core.network.impl.character.ICharacterNetworkService
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import okio.IOException
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.BlockJUnit4ClassRunner

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(BlockJUnit4ClassRunner::class)
class CharacterRepositoryTest {

    @MockK
    lateinit var apiService: ICharacterNetworkService
    @MockK
    lateinit var databaseService: ICharacterDatabaseService
    private lateinit var characterRepository: ICharacterRepository

    @Before
    fun setup() {
        MockKAnnotations.init(this)
        val dispatcher = UnconfinedTestDispatcher()
        Dispatchers.setMain(dispatcher)
        characterRepository = CharacterRepository(
            apiService = apiService,
            localDataSource = databaseService,
            ioDispatcher = dispatcher,
            errorParser = JikanErrorParserUtil()
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Function will call Network when database is empty`() = runTest {
        val animeID = 44511
        val characterEntities = chainsawManCharacters.map {
            it.asEntity(animeID)
        }
        coEvery { databaseService.insertOrUpdateCharacter(characterEntities) } returns Unit
        coEvery { databaseService.getAllCharacterFromAnimeById(animeID) } answers { emptyList() }
        coEvery { apiService.getCharactersByAnimeID(animeID) } answers { chainsawManCharacters }
        characterRepository.getAnimeCharactersById(animeID).collect {
            advanceUntilIdle()
            coVerify(atLeast = 1) { databaseService.insertOrUpdateCharacter(characterEntities) }
            coVerify(exactly = 2) { databaseService.getAllCharacterFromAnimeById(animeID) }
            coVerify(atLeast = 1) { apiService.getCharactersByAnimeID(animeID) }
        }
    }

    @Test
    fun `Function will not call Network when database is not empty`() = runTest {
        val animeID = 44511
        val characterEntities = chainsawManCharacters.map {
            it.asEntity(animeID)
        }
        coEvery { databaseService.insertOrUpdateCharacter(characterEntities) } returns Unit
        coEvery { databaseService.getAllCharacterFromAnimeById(animeID) } answers { characterEntities }
        coEvery { apiService.getCharactersByAnimeID(animeID) } answers { chainsawManCharacters }
        characterRepository.getAnimeCharactersById(animeID).collect {
            advanceUntilIdle()
            coVerify(exactly = 1) { databaseService.getAllCharacterFromAnimeById(animeID) }
            coVerify(inverse = true) { apiService.getCharactersByAnimeID(animeID) }
            coVerify(inverse = true) { databaseService.insertOrUpdateCharacter(characterEntities) }
        }
    }

    @Test
    fun `Function should return correct Response when Success and Call from network when it's empty`() =
        runTest {
            val characterID = 170733
            coEvery { apiService.getCharacterDetailByCharacterID(characterID) } returns powerCharacterDetail
            coEvery { databaseService.getCharacterAdditionalInformationById(characterID) } returns null
            coEvery { databaseService.insertOrReplaceAdditionalInformation(powerCharacterDetail.asEntity()) } returns Unit
            coEvery { databaseService.getCharacterFullProfile(characterID) } returns powerProfile
            val flowEmission =
                characterRepository.getCharacterDetailById(characterID).take(2).toList()
            Assert.assertEquals("Emission should be Loading", Resource.Loading, flowEmission[0])
            Assert.assertEquals(
                "Emission should be Result",
                Resource.Success(data = powerProfile.asCharacterDetail()).data,
                (flowEmission[1] as Resource.Success).data
            )
            coVerify(exactly = 1) {
                databaseService.getCharacterAdditionalInformationById(
                    characterID
                )
            }
            coVerify(exactly = 1) { apiService.getCharacterDetailByCharacterID(characterID) }
            coVerify(exactly = 1) {
                databaseService.insertOrReplaceAdditionalInformation(
                    powerCharacterDetail.asEntity()
                )
            }
            coVerify(exactly = 1) { databaseService.getCharacterFullProfile(characterID) }
        }

    @Test
    fun `Function should return correct error message when Exception happened`() = runTest {
        val characterID = 170733
        val message = "Test Exception message"
        coEvery { apiService.getCharacterDetailByCharacterID(characterID) } throws IOException(message)
        coEvery { databaseService.getCharacterAdditionalInformationById(characterID) } returns null
        coEvery { databaseService.insertOrReplaceAdditionalInformation(powerCharacterDetail.asEntity()) } returns Unit
        coEvery { databaseService.getCharacterFullProfile(characterID) } returns powerProfile
        val flowEmission =
            characterRepository.getCharacterDetailById(characterID).toList()
        Assert.assertEquals("Emission should be Loading", Resource.Loading, flowEmission[0])
        Assert.assertEquals(
            "Emission should be Result",
            Resource.Error(data = null, message = "Error: $message").message,
            (flowEmission[1] as Resource.Error).message
        )
        coVerify(exactly = 1) {
            databaseService.getCharacterAdditionalInformationById(
                characterID
            )
        }
        coVerify(exactly = 1) { apiService.getCharacterDetailByCharacterID(characterID) }
        coVerify(inverse = true) {
            databaseService.insertOrReplaceAdditionalInformation(
                powerCharacterDetail.asEntity()
            )
        }
        coVerify(inverse = true) { databaseService.getCharacterFullProfile(characterID) }
    }

    @Test
    fun `Function should return specific message when failed on parsing JSON error`() = runTest {
        val characterID = 170733
        val message = "Error: Response failed to parse"
        coEvery { apiService.getCharacterDetailByCharacterID(characterID) } throws IOException(message)
        coEvery { databaseService.getCharacterAdditionalInformationById(characterID) } returns null
        coEvery { databaseService.insertOrReplaceAdditionalInformation(powerCharacterDetail.asEntity()) } returns Unit
        coEvery { databaseService.getCharacterFullProfile(characterID) } returns powerProfile
        val flowEmission =
            characterRepository.getCharacterDetailById(characterID).toList()
        Assert.assertEquals("Emission should be Loading", Resource.Loading, flowEmission[0])
        Assert.assertEquals(
            "Emission should be Result",
            Resource.Error(data = null, message = "Error: $message").message,
            (flowEmission[1] as Resource.Error).message
        )
        coVerify(exactly = 1) {
            databaseService.getCharacterAdditionalInformationById(
                characterID
            )
        }
        coVerify(exactly = 1) { apiService.getCharacterDetailByCharacterID(characterID) }
        coVerify(inverse = true) {
            databaseService.insertOrReplaceAdditionalInformation(
                powerCharacterDetail.asEntity()
            )
        }
        coVerify(inverse = true) { databaseService.getCharacterFullProfile(characterID) }
    }
}
