package com.alexander.data.remote

class RemoteDataSource(private val api: ApiService) {

    suspend fun getPopularPersons(pageNumber: Int) = safeApiCall {
        api.getPopularPersons(pageNumber)
    }

    suspend fun getPopularPersonDetails(personId: Int) = safeApiCall {
        api.getPopularPersonDetails(personId)
    }

    suspend fun getPopularPersonImages(personId: Int) = safeApiCall {
        api.getPopularPersonImages(personId)
    }
}
