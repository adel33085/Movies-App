package com.alexander.data.remote

import com.alexander.data.remote.entity.ApiPopularPersonsResult
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("person/popular")
    suspend fun getPopularPersons(
        @Query("page") pageNumber: Int
    ): ApiPopularPersonsResult
}
