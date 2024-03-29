package com.alexander.data.remote

class RemoteDataSource(private val api: ApiService) {

    suspend fun getPopularPersons(pageNumber: Int) = safeApiCall {
        api.getPopularPersons(pageNumber)
    }

    suspend fun getPopularPersonDetails(personId: Int) = safeApiCall {
        api.getPopularPersonDetails(personId)
    }

    suspend fun getPopularPersonImages(personId: Int) = safeApiCall {
        api.getPopularPersonImages(personId)
    }

    suspend fun searchPopularPersons(searchKeywords: String, pageNumber: Int) = safeApiCall {
        api.searchPopularPersons(searchKeywords, pageNumber)
    }

    suspend fun getTopRatedMovies(pageNumber: Int) = safeApiCall {
        api.getTopRatedMovies(pageNumber)
    }

    suspend fun getMovieCredits(movieId: Int) = safeApiCall {
        api.getMovieCredits(movieId)
    }
}
