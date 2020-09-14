package com.ddapps.reservoirreviews.di

import androidx.room.Room
import com.ddapps.reservoirreviews.BuildConfig
import com.ddapps.reservoirreviews.data.local.AppDataBase
import com.ddapps.reservoirreviews.data.remote.RestApi
import com.ddapps.reservoirreviews.data.repository.FavatoritesRepository
import com.ddapps.reservoirreviews.data.repository.MovieRepository
import com.ddapps.reservoirreviews.domain.common.networking.ResponseHandler
import com.ddapps.reservoirreviews.domain.usecase.DisplayReviewsUseCase
import com.ddapps.reservoirreviews.domain.usecase.FavoritesReviewsUseCase
import com.ddapps.reservoirreviews.ui.viewmodel.DetailsViewModel
import com.ddapps.reservoirreviews.ui.viewmodel.FavoriteViewModel
import com.ddapps.reservoirreviews.ui.viewmodel.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


val networkModule = module {
    factory { ResponseHandler()}
    factory { RestApi()}
}

val viewModelModule = module {
    viewModel { HomeViewModel(get(), get()) }
    viewModel { DetailsViewModel(get(), get()) }
    viewModel { FavoriteViewModel(get()) }
}

val useCaseModule = module {
    factory { FavoritesReviewsUseCase(get())}
    factory { DisplayReviewsUseCase(get(), get()) }
}

val repositoryModule = module {
    factory { MovieRepository(get(), get(), get()) }
    factory { FavatoritesRepository(get(), get()) }

}

val dbModule = module {
    single { Room.databaseBuilder(
        get(),
        AppDataBase::class.java,
        "${ BuildConfig.APPLICATION_ID}.db")
        .build()}
    single { get<AppDataBase>().reviewDao() }
}

val modulesList = listOf(viewModelModule, repositoryModule, networkModule, dbModule, useCaseModule)