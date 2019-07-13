package com.alexander.data.remote.entity

import com.alexander.domain.entity.PopularPerson
import com.squareup.moshi.Json

data class ApiPopularPersonsResult(
    @field:Json(name = "page")
    val page: Int?,
    @field:Json(name = "total_results")
    val totalResults: Int?,
    @field:Json(name = "total_pages")
    val totalPages: Int?,
    @field:Json(name = "results")
    val popularPersons: List<ApiPopularPerson>?
)

data class ApiPopularPerson(
    @field:Json(name = "popularity")
    val popularity: Double?,
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "profile_path")
    val profilePath: String?,
    @field:Json(name = "name")
    val name: String?,
    @field:Json(name = "known_for")
    val knownFor: List<ApiKnownFor>?,
    @field:Json(name = "adult")
    val adult: Boolean?
)

data class ApiKnownFor(
    @field:Json(name = "original_name")
    val originalName: String?,
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "media_type")
    val mediaType: String?,
    @field:Json(name = "name")
    val name: String?,
    @field:Json(name = "vote_count")
    val voteCount: Int?,
    @field:Json(name = "vote_average")
    val voteAverage: Double?,
    @field:Json(name = "poster_path")
    val posterPath: String?,
    @field:Json(name = "first_air_date")
    val firstAirDate: String?,
    @field:Json(name = "popularity")
    val popularity: Double?,
    @field:Json(name = "genre_ids")
    val genreIds: List<Int>?,
    @field:Json(name = "original_language")
    val originalLanguage: String?,
    @field:Json(name = "backdrop_path")
    val backdropPath: String?,
    @field:Json(name = "overview")
    val overview: String?,
    @field:Json(name = "origin_country")
    val originCountry: List<String>?
)

fun ApiPopularPerson.toPopularPerson(): PopularPerson? {
    if (id != null
        && name != null
        && profilePath != null
    ) {
        return PopularPerson(
            id = id,
            name = name,
            profilePic = "https://image.tmdb.org/t/p/w300/$profilePath"
        )
    }
    return null
}
