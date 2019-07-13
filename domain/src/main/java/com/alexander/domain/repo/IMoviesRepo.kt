package com.alexander.domain.repo

import com.alexander.domain.entity.PopularPerson
import com.alexander.domain.entity.RequestResult

interface IMoviesRepo {
    suspend fun getPopularPersons(pageNumber: Int): RequestResult<List<PopularPerson>>
}
