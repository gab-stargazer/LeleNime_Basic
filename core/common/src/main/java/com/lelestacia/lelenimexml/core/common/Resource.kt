package com.lelestacia.lelenimexml.core.common

sealed class Resource<T>(val data: T? = null, val error: String? = null) {
    object Loading : Resource<Nothing>()
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(error: String) : Resource<T>(error = error)
}
