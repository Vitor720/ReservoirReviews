package com.ddapps.reservoirreviews.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ddapps.reservoirreviews.data.local.entity.ReviewEntity

@Dao
interface ReviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun add(movies : List<ReviewEntity>)

    @Query("SELECT * FROM review")
    fun getAllRevies() : List<ReviewEntity>

    @Query("SELECT * FROM review WHERE movie_title LIKE :title")
    fun getReviewByMovieTitle(title : String) : List<ReviewEntity>
}