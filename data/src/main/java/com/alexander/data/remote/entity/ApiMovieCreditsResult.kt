package com.alexander.data.remote.entity

import com.alexander.domain.entity.Actor
import com.squareup.moshi.Json

data class ApiMovieCreditsResult(
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "cast")
    val cast: List<ApiCast>?,
    @field:Json(name = "crew")
    val crew: List<ApiCrew>?
)

data class ApiCast(
    @field:Json(name = "cast_id")
    val castId: Int?,
    @field:Json(name = "character")
    val character: String?,
    @field:Json(name = "credit_id")
    val creditId: String?,
    @field:Json(name = "gender")
    val gender: Int?,
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "name")
    val name: String?,
    @field:Json(name = "order")
    val order: Int?,
    @field:Json(name = "profile_path")
    val profilePath: String?
)

data class ApiCrew(
    @field:Json(name = "credit_id")
    val creditId: String?,
    @field:Json(name = "department")
    val department: String?,
    @field:Json(name = "gender")
    val gender: Int?,
    @field:Json(name = "id")
    val id: Int?,
    @field:Json(name = "job")
    val job: String?,
    @field:Json(name = "name")
    val name: String?,
    @field:Json(name = "profile_path")
    val profilePath: String?
)

fun ApiCast.toActor(): Actor? {
    if (id != null
        && profilePath != null
    ) {
        return Actor(
            actorId = id,
            actorPic = "https://image.tmdb.org/t/p/w300/$profilePath"
        )
    }
    return null
}
