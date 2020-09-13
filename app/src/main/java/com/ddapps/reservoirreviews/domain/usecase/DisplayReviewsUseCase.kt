package com.ddapps.reservoirreviews.domain.usecase

import com.ddapps.reservoirreviews.data.local.entity.ReviewEntity
import com.ddapps.reservoirreviews.data.remote.models.MovieDataResponse
import com.ddapps.reservoirreviews.data.repository.MovieRepository
import com.ddapps.reservoirreviews.domain.common.model.MovieDisplay
import com.ddapps.reservoirreviews.domain.common.networking.Resource
import com.ddapps.reservoirreviews.domain.common.networking.ResponseHandler
import com.ddapps.reservoirreviews.utils.mapForView

class DisplayReviewsUseCase(private val reviewsRepo: MovieRepository, private val handle: ResponseHandler) {

   suspend fun getReviewsByMovieTitle(title: String): Resource<List<MovieDisplay>> {
       val localResponse = getLocalMoviesByName(title)
       return if (localResponse.isNullOrEmpty()){
           retriveRemoteList(title)
       } else {
          Resource.success(localResponse.mapForView())
      }
    }

    suspend fun getReviewByMovieTitle(title: String): Resource<MovieDisplay>{
        val localResponse = reviewsRepo.getLocalSingleReview(title)
        return Resource.success(localResponse.data?.mapForView())

    }

    private suspend fun retriveRemoteList(title: String): Resource<List<MovieDisplay>> {
        val apiResponse = getApiMoviesByTitle(title)
        storeReviewList(apiResponse)
        val localResponse = reviewsRepo.getLocalMoviesByName(title)
        val movieList = localResponse.data!!.mapForView()
        return if (movieList.isNullOrEmpty()){
            Resource.error("Titulo não encontrado", null)
        } else {
            Resource.success(movieList)
        }
    }

    private suspend fun getLocalMoviesByName(title: String): List<ReviewEntity>? {
        return reviewsRepo.getLocalMoviesByName(title).data
    }

    private suspend fun getApiMoviesByTitle(title: String): Resource<MovieDataResponse> {
        return reviewsRepo.getRemoteMoviesByName(title)
    }

    private suspend fun storeReviewList(apiResponse: Resource<MovieDataResponse>) {
        reviewsRepo.storeReviews(apiResponse.data!!.results )
    }


}
