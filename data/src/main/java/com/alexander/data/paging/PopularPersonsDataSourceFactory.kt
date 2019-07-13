package com.alexander.data.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.alexander.domain.entity.PopularPerson
import com.alexander.domain.repo.IMoviesRepo

class PopularPersonsDataSourceFactory(
    private val moviesRepo: IMoviesRepo,
    private val searchKeywords: String?
) : DataSource.Factory<Int, PopularPerson>() {

    val sourceLiveData = MutableLiveData<PopularPersonsPageKeyedDataSource>()

    override fun create(): DataSource<Int, PopularPerson> {
        val source = PopularPersonsPageKeyedDataSource(moviesRepo, searchKeywords)
        sourceLiveData.postValue(source)
        return source
    }
}
