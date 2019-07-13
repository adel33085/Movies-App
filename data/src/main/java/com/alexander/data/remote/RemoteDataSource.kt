package com.alexander.data.remote

class RemoteDataSource(private val api: ApiService) {

    suspend fun getPopularPersons(pageNumber: Int) = safeApiCall {
        api.getPopularPersons(pageNumber)
    }
}
