package com.ddapps.reservoirreviews.data.remote.models

data class ResultDataResponse(
    val byline: String,
    val critics_pick: Int,
    val date_updated: String,
    val display_title: String,
    val headline: String,
    val link: LinkDataResponse,
    val mpaa_rating: String,
    val multimedia: MultimediaDataResponse,
    val opening_date: String,
    val publication_date: String,
    val summary_short: String
)