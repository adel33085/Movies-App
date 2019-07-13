package com.alexander.data.remote

import com.alexander.domain.entity.RequestResult
import retrofit2.HttpException
import java.io.IOException

suspend fun <T> safeApiCall(apiCall: suspend () -> T): RequestResult<T> {
    return try {
        return RequestResult.Success(apiCall())
    } catch (e: Exception) {
        when (e) {
            is HttpException -> {
                when (e.code()) {
                    501 -> {
                        RequestResult.Error(Exception("Invalid service: this service does not exist."))
                    }
                    401 -> {
                        RequestResult.Error(Exception("Authentication failed: You do not have permissions to access the service."))
                    }
                    405 -> {
                        RequestResult.Error(Exception("Invalid format: This service doesn't exist in that format."))
                    }
                    500 -> {
                        RequestResult.Error(Exception("Internal error: Something went wrong, contact TMDb."))
                    }
                    else -> {
                        RequestResult.Error(Exception("Something went wrong !"))
                    }
                }
            }
            is IOException -> {
                RequestResult.Error(Exception("No internet connection"))
            }
            else -> {
                RequestResult.Error(Exception("Something went wrong !"))
            }
        }
    }
}
