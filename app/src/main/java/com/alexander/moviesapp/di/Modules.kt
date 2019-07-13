package com.alexander.moviesapp.di

import com.alexander.data.remote.Network
import com.alexander.data.remote.RemoteDataSource
import com.alexander.data.repo.MoviesRepo
import com.alexander.domain.repo.IMoviesRepo
import com.alexander.moviesapp.ui.popularPersonDetails.PopularPersonDetailsViewModel
import com.alexander.moviesapp.ui.popularPersons.PopularPersonsViewModel
import com.alexander.moviesapp.ui.repeatedActors.RepeatedActorsViewModel
import com.alexander.moviesapp.ui.search.SearchViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.Module
import org.koin.dsl.module

private val network = module {
    factory { Network.apiService }
}

private val remoteModule = module {
    factory { RemoteDataSource(get()) }
}

private val repositoryModule = module {
    single<IMoviesRepo> { MoviesRepo(get()) }
}

private val viewModelModule = module {
    viewModel { PopularPersonsViewModel(get()) }
    viewModel { PopularPersonDetailsViewModel(get()) }
    viewModel { SearchViewModel(get()) }
    viewModel { RepeatedActorsViewModel(get()) }
}

fun getModules(): Array<Module> {
    return arrayOf(
        network,
        remoteModule,
        repositoryModule,
        viewModelModule
    )
}
