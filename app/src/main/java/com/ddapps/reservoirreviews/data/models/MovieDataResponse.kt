package com.ddapps.reservoirreviews.data.models

data class MovieDataResponse(
    val copyright: String,
    val has_more: Boolean,
    val num_results: Int,
    val results: List<ResultDataResponse>,
    val status: String
)