package com.ddapps.reservoirreviews.di

import com.ddapps.reservoirreviews.data.remote.RestApi
import com.ddapps.reservoirreviews.data.repository.MovieRepository
import com.ddapps.reservoirreviews.domain.common.networking.ResponseHandler
import org.koin.dsl.module


val networkModule = module {
    factory { ResponseHandler()}
    factory { RestApi()}
}

val viewModelModule = module {

}

val repositoryModule = module {
    factory { MovieRepository(get(), get()) }

}

val modulesList = listOf(viewModelModule, repositoryModule, networkModule)