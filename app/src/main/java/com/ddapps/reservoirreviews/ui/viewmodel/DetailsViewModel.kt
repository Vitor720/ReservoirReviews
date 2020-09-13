package com.ddapps.reservoirreviews.ui.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ddapps.reservoirreviews.domain.common.model.MovieDisplay
import com.ddapps.reservoirreviews.domain.common.networking.Resource
import com.ddapps.reservoirreviews.domain.usecase.DisplayReviewsUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailsViewModel(private val reviewUseCase: DisplayReviewsUseCase) : ViewModel() {

    private var review = MutableLiveData<Resource<MovieDisplay>>()

    fun getMovieReview() = review

    fun getSingleReviewByTitle(title: String){
        CoroutineScope(Dispatchers.IO).launch {
            review.postValue(Resource.loading(null))
            review.postValue(reviewUseCase.getReviewByMovieTitle(title))
        }
    }
}