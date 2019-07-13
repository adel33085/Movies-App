package com.alexander.moviesapp.ui.popularPersons

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.alexander.data.paging.PopularPersonsDataSourceFactory
import com.alexander.data.remote.NetworkState
import com.alexander.domain.entity.PopularPerson
import com.alexander.domain.repo.IMoviesRepo

class PopularPersonsViewModel(
    private val moviesRepo: IMoviesRepo
) : ViewModel() {

    val dataSourceFactory: PopularPersonsDataSourceFactory
    val popularPersonsList: LiveData<PagedList<PopularPerson>>
    var initialNetworkState: LiveData<NetworkState>
    var networkState: LiveData<NetworkState>

    init {
        val config = PagedList
            .Config
            .Builder()
            .setInitialLoadSizeHint(20)
            .setPageSize(20)
            .setEnablePlaceholders(false)
            .build()

        dataSourceFactory = PopularPersonsDataSourceFactory(moviesRepo)

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
