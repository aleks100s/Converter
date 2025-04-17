package com.alextos.common.data

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

suspend inline fun <reified T> safeCall(
    execute: () -> HttpResponse
): Result<T> {
    return try {
        val response = execute()
        if (response.status.value in 200..299) {
            Result.success(response.body() as T)
        } else {
            Result.failure(Exception("Error: ${response.status.description}"))
        }
    } catch (e: Exception) {
        Result.failure(e)
    }
}

suspend inline fun <reified T> responseToResult(
    response: HttpResponse
): Result<T> {
    return when(response.status.value) {
        in 200..299 -> {
            Result.success(response.body<T>())
        }
        in 400..499 -> {
            val errorBody = response.status.description
            Result.failure(Exception("Client error: ${response.status.description}"))
        }
        in 500..599 -> {
            Result.failure(Exception("Server error: ${response.status.description}"))
        }
        else -> {
            Result.failure(Exception("Unknown error: ${response.status.description}"))
        }
    }
}