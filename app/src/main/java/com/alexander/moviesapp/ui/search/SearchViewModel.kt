package com.alexander.moviesapp.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.alexander.data.paging.PopularPersonsDataSourceFactory
import com.alexander.data.remote.NetworkState
import com.alexander.domain.entity.PopularPerson
import com.alexander.domain.repo.IMoviesRepo

class SearchViewModel(private val moviesRepo: IMoviesRepo) : ViewModel() {

    lateinit var dataSourceFactory: PopularPersonsDataSourceFactory
    lateinit var popularPersonsList: LiveData<PagedList<PopularPerson>>
    lateinit var initialNetworkState: LiveData<NetworkState>
    lateinit var networkState: LiveData<NetworkState>

    fun getPopularPersons(searchKeywords: String) {
        val config = PagedList
            .Config
            .Builder()
            .setInitialLoadSizeHint(20)
            .setPageSize(20)
            .setEnablePlaceholders(false)
            .build()

        dataSourceFactory = PopularPersonsDataSourceFactory(moviesRepo, searchKeywords)

        popularPersonsList = LivePagedListBuilder(dataSourceFactory, config).build()

        initialNetworkState = Transformations.switchMap(dataSourceFactory.sourceLiveData) {
            it.initialNetworkState
        }

        networkState = Transformations.switchMap(dataSourceFactory.sourceLiveData) {
            it.networkState
        }
    }

    fun retry() {
        dataSourceFactory.sourceLiveData.value?.retry()
    }
}
