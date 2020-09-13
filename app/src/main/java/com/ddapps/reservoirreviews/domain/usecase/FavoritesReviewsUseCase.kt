package com.ddapps.reservoirreviews.domain.usecase

import com.ddapps.reservoirreviews.data.repository.FavatoritesRepository
import com.ddapps.reservoirreviews.domain.common.model.MovieDisplay
import com.ddapps.reservoirreviews.domain.common.networking.Resource
import com.ddapps.reservoirreviews.utils.mapForView

class FavoritesReviewsUseCase(private val favoriteRepo: FavatoritesRepository) {

  suspend fun bookMarkReview(title: String) {
      favoriteRepo.setReviewFavorite(title)
    }

    suspend fun getAllFavoriteReviews(): Resource<List<MovieDisplay>>{
        val favorites = favoriteRepo.getAllFavorites()
        return Resource.success(favorites.data?.mapForView() ?: listOf())
    }

}
