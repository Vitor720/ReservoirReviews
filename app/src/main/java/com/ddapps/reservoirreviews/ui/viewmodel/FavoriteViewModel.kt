package com.ddapps.reservoirreviews.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ddapps.reservoirreviews.domain.common.model.MovieDisplay
import com.ddapps.reservoirreviews.domain.common.networking.Resource
import com.ddapps.reservoirreviews.domain.usecase.FavoritesReviewsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel(private val useCase: FavoritesReviewsUseCase): ViewModel() {

    private var favoritesList = MutableLiveData<Resource<List<MovieDisplay>>>()

    fun getFavoritesList() =  favoritesList

    fun getAllFavoriteRevies() {
        CoroutineScope(Dispatchers.IO).launch {
            favoritesList.postValue(Resource.loading(null))
            val response = useCase.getAllFavoriteReviews()
            favoritesList.postValue(response)
        }
    }

}
