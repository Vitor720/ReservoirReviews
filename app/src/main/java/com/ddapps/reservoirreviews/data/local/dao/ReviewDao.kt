package com.ddapps.reservoirreviews.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ddapps.reservoirreviews.data.local.entity.ReviewEntity

@Dao
interface ReviewDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
   suspend fun add(movie: List<ReviewEntity>)

    @Query("SELECT * FROM review WHERE user_favorite = 1")
    suspend fun getFavoriteReviews() : List<ReviewEntity>

    @Query("SELECT * FROM review WHERE movie_title LIKE '%' || :title || '%'")
    suspend fun getListReviewsByMovieTitle(title : String) : List<ReviewEntity>

    @Query("SELECT * FROM review WHERE movie_title = :title")
    suspend fun getSingleReviewByMovieTitle(title: String): ReviewEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateReviewToFavorite(review: ReviewEntity)

}