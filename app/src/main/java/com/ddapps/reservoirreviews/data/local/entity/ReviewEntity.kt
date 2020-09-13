package com.ddapps.reservoirreviews.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "review"
)

data class ReviewEntity(
    val id : Int?,
    @PrimaryKey
    val movie_title : String,
    val movie_img_url: String,
    val movie_img_local: String,
    val movie_release_date: String,
    val critics_name: String,
    val critics_choise: Int,
    val critics_short_review: String,
    val complete_review_link: String,
    var user_favorite: Int)