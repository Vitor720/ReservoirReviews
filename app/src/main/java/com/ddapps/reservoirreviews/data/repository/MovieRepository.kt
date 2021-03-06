package com.ddapps.reservoirreviews.data.repository

import com.ddapps.reservoirreviews.PusherApplication
import com.ddapps.reservoirreviews.data.local.AppDataBase
import com.ddapps.reservoirreviews.data.local.dao.ReviewDao
import com.ddapps.reservoirreviews.data.local.entity.ReviewEntity
import com.ddapps.reservoirreviews.data.remote.models.MovieDataResponse
import com.ddapps.reservoirreviews.data.remote.RestApi
import com.ddapps.reservoirreviews.data.remote.models.ResultDataResponse
import com.ddapps.reservoirreviews.domain.common.networking.Resource
import com.ddapps.reservoirreviews.domain.common.networking.ResponseHandler
import com.ddapps.reservoirreviews.utils.mapForRoom
import timber.log.Timber

class MovieRepository(private val movieApi: RestApi,
                      private val responseHandler: ResponseHandler,
                      private val dataBase: ReviewDao
) {
    suspend fun getRemoteMoviesByName(name: String): Resource<MovieDataResponse> {
        return try {
            val response = movieApi.getMoviesByTitle(name)
            return responseHandler.handleSuccess(response)
        } catch (t: Throwable) {
            responseHandler.handleThrowable(t)
        }
    }

    suspend fun getLocalMoviesByName(name: String): Resource<List<ReviewEntity>> {
        return try {
            val response = dataBase.getListReviewsByMovieTitle(name)
            return responseHandler.handleSuccess(response)
        } catch (t: Throwable) {
            responseHandler.handleThrowable(t)
        }
    }

    suspend fun getLocalSingleReview(title: String): Resource<ReviewEntity>{
        return try {
            val response = dataBase.getSingleReviewByMovieTitle(title)
            return responseHandler.handleSuccess(response)
        } catch (t: Throwable){
            responseHandler.handleThrowable(t)
        }
    }



   suspend fun storeReviews(reviews: List<ResultDataResponse>){
        try{
            val entitys = reviews.mapForRoom()
            dataBase.add(entitys)
        }catch (t: Throwable){
            Timber.e("Falhou por ${t.message}")
        }
    }
}