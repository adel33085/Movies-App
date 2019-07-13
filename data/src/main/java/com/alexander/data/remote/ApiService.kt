package com.alexander.data.remote

import com.alexander.data.remote.entity.ApiPopularPersonDetailsResult
import com.alexander.data.remote.entity.ApiPopularPersonImagesResult
import com.alexander.data.remote.entity.ApiPopularPersonsResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("person/popular")
    suspend fun getPopularPersons(
        @Query("page") pageNumber: Int
    ): ApiPopularPersonsResult

    @GET("person/{person_id}")
    suspend fun getPopularPersonDetails(
        @Path("person_id") personId: Int
    ): ApiPopularPersonDetailsResult

    @GET("person/{person_id}/images")
    suspend fun getPopularPersonImages(
        @Path("person_id") personId: Int
    ): ApiPopularPersonImagesResult
}
