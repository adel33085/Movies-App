package com.alexander.data.remote.entity

import com.alexander.domain.entity.PopularPersonDetails
import com.squareup.moshi.Json

data class ApiPopularPersonDetailsResult(
    @field:Json(name = "birthday")
    val birthday: String?,
    @field:Json(name = "known_for_department")
    val knownForDepartment: String?,
    @field:Json(name = "deathday")
    val deathDay: String?,
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "name")
    val name: String?,
    @field:Json(name = "also_known_as")
    val alsoKnownAs: List<String>?,
    @field:Json(name = "gender")
    val gender: Int?,
    @field:Json(name = "biography")
    val biography: String?,
    @field:Json(name = "popularity")
    val popularity: Double?,
    @field:Json(name = "place_of_birth")
    val placeOfBirth: String?,
    @field:Json(name = "profile_path")
    val profilePath: String?,
    @field:Json(name = "adult")
    val adult: Boolean?,
    @field:Json(name = "imdb_id")
    val imdbId: String?,
    @field:Json(name = "homepage")
    val homepage: String?
)

data class ApiPopularPersonImagesResult(
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "profiles")
    val images: List<ApiImage>?
)

data class ApiImage(
    @field:Json(name = "iso_639_1")
    val iso: String?,
    @field:Json(name = "width")
    val width: Int?,
    @field:Json(name = "height")
    val height: Int?,
    @field:Json(name = "vote_count")
    val voteCount: Int?,
    @field:Json(name = "vote_average")
    val voteAverage: Double?,
    @field:Json(name = "file_path")
    val filePath: String?,
    @field:Json(name = "aspect_ratio")
    val aspectRatio: Double?
)

fun ApiPopularPersonDetailsResult.toPopularPersonDetails(images: List<String>?): PopularPersonDetails? {
    if (id != null
        && name != null
        && profilePath != null
        && biography != null
    ) {
        return PopularPersonDetails(
            id = id,
            name = name,
            profilePic = "https://image.tmdb.org/t/p/w300/$profilePath",
            biography = biography,
            birthday = birthday,
            placeOfBirth = placeOfBirth,
            images = images
        )
    }
    return null
}

fun ApiPopularPersonImagesResult.toImages(): List<String>? {
    if (!images.isNullOrEmpty()) {
        return images
            .map { it.filePath }
            .map { "https://image.tmdb.org/t/p/w300/$it" }
    }
    return null
}
