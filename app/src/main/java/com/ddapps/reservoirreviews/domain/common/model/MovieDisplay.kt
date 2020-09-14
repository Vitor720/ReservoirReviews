package com.ddapps.reservoirreviews.domain.common.model

class MovieDisplay(val movieTitle : String,
                   val movieImage : String,
                   val releaseDate: String,
                   val shortReview: String,
                   val reviewLink : String,
                   val criticsName: String,
                   val criticsPick: Boolean) {

    var swiped: Boolean = false

    fun getReviewAuthor(): String{
        return "by $criticsName"
    }

    fun getDisplayDate(): String{
        return "released: $releaseDate"
    }

}