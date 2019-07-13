package com.alexander.data.remote

import com.alexander.data.remote.entity.*
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

    @GET("search/person")
    suspend fun searchPopularPersons(
        @Query("query") searchKeywords: String,
        @Query("page") pageNumber: Int
    ): ApiPopularPersonsResult

    @GET("movie/top_rated")
    suspend fun getTopRatedMovies(
        @Query("page") pageNumber: Int
    ): ApiTopRatedMoviesResult

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int
    ): ApiMovieCreditsResult
}
