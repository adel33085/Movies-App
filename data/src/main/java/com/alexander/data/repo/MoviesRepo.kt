package com.alexander.data.repo

import com.alexander.data.remote.RemoteDataSource
import com.alexander.data.remote.entity.toImages
import com.alexander.data.remote.entity.toPopularPerson
import com.alexander.data.remote.entity.toPopularPersonDetails
import com.alexander.domain.entity.PopularPerson
import com.alexander.domain.entity.PopularPersonDetails
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

    override suspend fun getPopularPersonDetails(
        personId: Int
    ): RequestResult<PopularPersonDetails> {
        val images = getPopularPersonImages(personId)
        return when (val result = remoteDataSource.getPopularPersonDetails(personId)) {
            is RequestResult.Success -> {
                val data = result.data.toPopularPersonDetails(images)
                if (data != null) {
                    RequestResult.Success(data)
                } else {
                    RequestResult.Error(Exception("Something went wrong !"))
                }
            }
            is RequestResult.Error -> {
                RequestResult.Error(result.exception)
            }
        }
    }

    private suspend fun getPopularPersonImages(personId: Int): List<String>? {
        val images: List<String>?
        when (val result = remoteDataSource.getPopularPersonImages(personId)) {
            is RequestResult.Success -> {
                images = result.data.toImages()
            }
            is RequestResult.Error -> {
                images = null
            }
        }
        return images
    }
}
