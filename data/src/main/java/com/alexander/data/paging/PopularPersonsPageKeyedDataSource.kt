package com.alexander.data.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.alexander.data.remote.NetworkState
import com.alexander.domain.entity.PopularPerson
import com.alexander.domain.entity.RequestResult
import com.alexander.domain.repo.IMoviesRepo
import kotlinx.coroutines.runBlocking

class PopularPersonsPageKeyedDataSource(
    private val moviesRepo: IMoviesRepo,
    private val searchKeywords: String?
) : PageKeyedDataSource<Int, PopularPerson>() {

    private var retry: (() -> Any)? = null

    val networkState = MutableLiveData<NetworkState>()

    val initialNetworkState = MutableLiveData<NetworkState>()

    fun retry() {
        retry?.invoke()
    }

    override fun loadInitial(params: LoadInitialParams<Int>, callback: LoadInitialCallback<Int, PopularPerson>) {
        networkState.postValue(NetworkState.LOADING)
        initialNetworkState.postValue(NetworkState.LOADING)
        runBlocking {
            val result = if (searchKeywords.isNullOrEmpty()) {
                moviesRepo.getPopularPersons(1)
            } else {
                moviesRepo.searchPopularPersons(searchKeywords, 1)
            }
            when (result) {
                is RequestResult.Success -> {
                    retry = null
                    if (!result.data.isNullOrEmpty()) {
                        networkState.postValue(NetworkState.LOADED)
                        initialNetworkState.postValue(NetworkState.LOADED)
                        callback.onResult(result.data, null, 2)
                    } else {
                        val error = NetworkState.error("No result found !")
                        networkState.postValue(error)
                        initialNetworkState.postValue(error)
                    }
                }
                is RequestResult.Error -> {
                    retry = {
                        loadInitial(params, callback)
                    }
                    val error = NetworkState.error(result.exception.message)
                    networkState.postValue(error)
                    initialNetworkState.postValue(error)
                }
            }
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, PopularPerson>) {}

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, PopularPerson>) {
        networkState.postValue(NetworkState.LOADING)
        runBlocking {
            val result = if (searchKeywords.isNullOrEmpty()) {
                moviesRepo.getPopularPersons(params.key)
            } else {
                moviesRepo.searchPopularPersons(searchKeywords, params.key)
            }
            when (result) {
                is RequestResult.Success -> {
                    retry = null
                    callback.onResult(result.data, params.key + 1)
                    networkState.postValue(NetworkState.LOADED)
                }
                is RequestResult.Error -> {
                    retry = {
                        loadAfter(params, callback)
                    }
                    networkState.postValue(NetworkState.error(result.exception.message))
                }
            }
        }
    }
}
