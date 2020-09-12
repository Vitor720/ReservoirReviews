package com.ddapps.reservoirreviews.domain.usecase

import com.ddapps.reservoirreviews.data.remote.models.MovieDataResponse
import com.ddapps.reservoirreviews.data.repository.MovieRepository
import com.ddapps.reservoirreviews.domain.common.model.MovieDisplay
import com.ddapps.reservoirreviews.domain.common.networking.Resource
import com.ddapps.reservoirreviews.domain.common.networking.ResponseHandler
import com.ddapps.reservoirreviews.utils.mapForView

class DisplayReviewsUseCase(private val reviewsRepo: MovieRepository, private val handler: ResponseHandler) {

   suspend fun getReviewsByMovieTitle(title: String): Resource<List<MovieDisplay>> {
       val localResponse = reviewsRepo.getLocalMoviesByName(title).data
       return if (localResponse.isNullOrEmpty()){
           val apiResponse = reviewsRepo.getRemoteMoviesByName(title)
           reviewsRepo.storeReviews(apiResponse.data!!.results)
           val list = reviewsRepo.getLocalMoviesByName(title).data!!.mapForView()
           Resource.success(list)
       } else {
          Resource.success(localResponse.mapForView())
      }
    }

}
