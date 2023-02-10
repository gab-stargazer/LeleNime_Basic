package com.lelestacia.lelenimexml.core.network.source

import com.lelestacia.lelenimexml.core.network.model.GenericResponse
import com.lelestacia.lelenimexml.core.network.model.character.NetworkCharacterDetail
import retrofit2.http.GET
import retrofit2.http.Path

interface CharacterAPI {

    @GET("characters/{id}/full")
    suspend fun getCharacterDetailByCharacterID(@Path("id") id: Int): GenericResponse<NetworkCharacterDetail>
}
