package com.ddapps.reservoirreviews.di

import androidx.room.Room
import com.ddapps.reservoirreviews.BuildConfig
import com.ddapps.reservoirreviews.data.local.AppDataBase
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
    factory { MovieRepository(get(), get(), get()) }

}

val dbModule = module {
    single { Room.databaseBuilder(
        get(),
        AppDataBase::class.java,
        "${ BuildConfig.APPLICATION_ID}.db")
        .build()}
    single { get<AppDataBase>().reviewDao() }
}

val modulesList = listOf(viewModelModule, repositoryModule, networkModule, dbModule)