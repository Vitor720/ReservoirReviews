package com.ddapps.reservoirreviews.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ddapps.reservoirreviews.domain.common.model.MovieDisplay
import com.ddapps.reservoirreviews.domain.common.networking.Resource
import com.ddapps.reservoirreviews.domain.usecase.DisplayReviewsUseCase
import com.ddapps.reservoirreviews.domain.usecase.FavoritesReviewsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(private val reviewUseCase: DisplayReviewsUseCase, private val bookMarkUseCase: FavoritesReviewsUseCase): ViewModel() {

    var isError: Boolean = false
    var isLoading: Boolean = true

    private var reviewsList = MutableLiveData<Resource<List<MovieDisplay>>>()

    fun getReviewList() =  reviewsList

    fun loadReviews(title: String, offset: Int, limit: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            reviewsList.postValue(Resource.loading(null))
            reviewsList.postValue(reviewUseCase.getReviewsByMovieTitle(title))
        }
    }
}