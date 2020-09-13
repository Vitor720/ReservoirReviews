package com.ddapps.reservoirreviews.data.repository

import com.ddapps.reservoirreviews.data.local.dao.ReviewDao
import com.ddapps.reservoirreviews.data.local.entity.ReviewEntity
import com.ddapps.reservoirreviews.data.remote.models.ResultDataResponse
import com.ddapps.reservoirreviews.domain.common.model.MovieDisplay
import com.ddapps.reservoirreviews.domain.common.networking.Resource
import com.ddapps.reservoirreviews.domain.common.networking.ResponseHandler
import com.ddapps.reservoirreviews.utils.mapForRoom
import timber.log.Timber

class FavatoritesRepository(private val responseHandler: ResponseHandler, private val dataBase: ReviewDao) {


    suspend fun setReviewFavorite(title: String){
        val entity = getLocalSingleReview(title).data
        entity?.user_favorite = 1
        storeFavoriteReview(entity!!)
    }

    private suspend fun getLocalSingleReview(title: String): Resource<ReviewEntity> {
        return try {
            val response = dataBase.getSingleReviewByMovieTitle(title)
            return responseHandler.handleSuccess(response)
        } catch (t: Throwable){
            responseHandler.handleThrowable(t)
        }
    }

    private suspend fun storeFavoriteReview(review: ReviewEntity){
        try{
            dataBase.updateReviewToFavorite(review)
            Timber.e("Concluiu")
        }catch (t: Throwable){
            Timber.e("Falhou por ${t.message}")
        }
    }

   suspend fun getAllFavorites(): Resource<List<ReviewEntity>> {
      return  try{
           val localResponse = dataBase.getFavoriteReviews()
           responseHandler.handleSuccess(localResponse)
       }catch (t: Throwable){
           responseHandler.handleThrowable(t)
       }
    }

}
