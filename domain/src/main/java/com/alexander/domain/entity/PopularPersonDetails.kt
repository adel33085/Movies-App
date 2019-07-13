package com.alexander.domain.entity

data class PopularPersonDetails(
    val id: Int,
    val name: String,
    val profilePic: String,
    val biography: String,
    val birthday: String?,
    val placeOfBirth: String?,
    var images: List<String>?
)
