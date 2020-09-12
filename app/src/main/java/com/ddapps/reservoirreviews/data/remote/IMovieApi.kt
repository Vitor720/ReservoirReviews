package com.ddapps.reservoirreviews.data.remote

import com.ddapps.reservoirreviews.data.remote.models.MovieDataResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface IMovieApi {

    @GET("reviews/search.json")
    suspend fun getMoviesByName(
        @Query("query") title: String,
        @Query("api-key") apiKey: String,
        @Query("offset") offset:Int): MovieDataResponse
}
