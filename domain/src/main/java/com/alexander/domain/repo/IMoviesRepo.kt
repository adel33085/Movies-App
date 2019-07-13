package com.alexander.domain.repo

import com.alexander.domain.entity.Actor
import com.alexander.domain.entity.PopularPerson
import com.alexander.domain.entity.PopularPersonDetails
import com.alexander.domain.entity.RequestResult

interface IMoviesRepo {
    suspend fun getPopularPersons(pageNumber: Int): RequestResult<List<PopularPerson>>
    suspend fun getPopularPersonDetails(personId: Int): RequestResult<PopularPersonDetails>
    suspend fun searchPopularPersons(searchKeywords: String, pageNumber: Int): RequestResult<List<PopularPerson>>
    suspend fun getRepeatedActors(): RequestResult<List<Actor>>
}
