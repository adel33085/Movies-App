package com.alexander.data.repo

import com.alexander.data.remote.RemoteDataSource
import com.alexander.data.remote.entity.*
import com.alexander.domain.entity.Actor
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

    override suspend fun searchPopularPersons(
        searchKeywords: String,
        pageNumber: Int
    ): RequestResult<List<PopularPerson>> {
        return when (val result = remoteDataSource.searchPopularPersons(searchKeywords, pageNumber)) {
            is RequestResult.Success -> {
                val data = result.data.popularPersons?.mapNotNull { it.toPopularPerson() } ?: emptyList()
                RequestResult.Success(data)
            }
            is RequestResult.Error -> {
                RequestResult.Error(result.exception)
            }
        }
    }

    private val allActors = mutableListOf<Actor>()

    private val repeatedActors = mutableListOf<Actor>()

    override suspend fun getRepeatedActors(): RequestResult<List<Actor>> {
        allActors.clear()
        repeatedActors.clear()
        return when (val result = remoteDataSource.getTopRatedMovies(1)) {
            is RequestResult.Success -> {
                val moviesList = result.data.movies
                RequestResult.Success(getActors(moviesList))
            }
            is RequestResult.Error -> {
                RequestResult.Error(result.exception)
            }
        }
    }

    private suspend fun getActors(moviesList: List<ApiMovie?>?): List<Actor> {
        if (!moviesList.isNullOrEmpty()) {
            for (movie in moviesList) {
                when (val result = remoteDataSource.getMovieCredits(movie?.id!!)) {
                    is RequestResult.Success -> {
                        val actorsList = result.data.cast?.mapNotNull { it.toActor() } ?: emptyList()
                        if (!actorsList.isNullOrEmpty()) {
                            for (actor in actorsList) {
                                if (!contains(allActors, actor)) {
                                    allActors.add(actor)
                                } else {
                                    if (!contains(repeatedActors, actor)) {
                                        repeatedActors.add(actor)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return repeatedActors
    }

    private fun contains(actorsList: List<Actor>, actor: Actor): Boolean {
        for (actorInList in actorsList) {
            if (actorInList.actorId == actor.actorId) {
                return true
            }
        }
        return false
    }
}
