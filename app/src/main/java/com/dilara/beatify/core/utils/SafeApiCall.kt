package com.dilara.beatify.core.utils

suspend fun <T> safeApiCall(
    apiCall: suspend () -> T
): Result<T> {
    return try {
        Result.success(apiCall())
    } catch (e: Exception) {
        Result.failure(e)
    }
}

suspend fun <T, R> safeApiCall(
    apiCall: suspend () -> T,
    transform: (T) -> R
): Result<R> {
    return try {
        val response = apiCall()
        Result.success(transform(response))
    } catch (e: Exception) {
        Result.failure(e)
    }
}
