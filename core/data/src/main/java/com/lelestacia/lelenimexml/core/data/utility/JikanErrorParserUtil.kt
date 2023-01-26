package com.lelestacia.lelenimexml.core.data.utility

import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.lelestacia.lelenimexml.core.network.model.ErrorResponse
import retrofit2.HttpException

class JikanErrorParserUtil {

    operator fun invoke(t: Throwable): String {
        if (t is HttpException) {
            val errorResponse = t.response()?.errorBody()
            val gson = Gson()
            val errorAdapter: TypeAdapter<ErrorResponse> = gson.getAdapter(ErrorResponse::class.java)
            return try {
                val error = errorAdapter.fromJson(errorResponse?.string())
                "Error ${error.status}: ${error.message}"
            } catch (e: Exception) {
                "Response failed to parse"
            }
        }

        return "Error: ${t.message}"
    }
}
