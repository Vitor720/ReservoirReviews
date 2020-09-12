package com.ddapps.reservoirreviews.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ddapps.reservoirreviews.data.local.entity.ReviewEntity

@Dao
interface ReviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
   suspend fun add(movie: List<ReviewEntity>)

    @Query("SELECT * FROM review WHERE user_favorite = 1")
    fun getFavoriteReviews() : List<ReviewEntity>

    @Query("SELECT * FROM review WHERE movie_title LIKE '%' || :title || '%'")
    suspend  fun getReviewByMovieTitle(title : String) : List<ReviewEntity>
}