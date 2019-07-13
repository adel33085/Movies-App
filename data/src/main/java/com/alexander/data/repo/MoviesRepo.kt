package com.alexander.data.repo

import com.alexander.data.remote.RemoteDataSource
import com.alexander.data.remote.entity.toPopularPerson
import com.alexander.domain.entity.PopularPerson
import com.alexander.domain.entity.RequestResult
import com.alexander.domain.repo.IMoviesRepo

class MoviesRepo(private val remoteDataSource: RemoteDataSource) : IMoviesRepo {

    override suspend fun getPopularPersons(
        pageNumber: Int
    ): RequestResult<List<PopularPerson>> {
        return when (val result = remoteDataSource.getPopularPersons(pageNumber)) {
            is RequestResult.Success -> {
                val data = result.data.popularPersons?.mapNotNull { it.toPopularPerson() } ?: emptyList()
                RequestResult.Success(data)
            }
            is RequestResult.Error -> {
                RequestResult.Error(result.exception)
            }
        }
    }
}
