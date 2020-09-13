package com.ddapps.reservoirreviews.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.ddapps.reservoirreviews.data.local.entity.ReviewEntity
import com.ddapps.reservoirreviews.data.remote.models.MultimediaDataResponse
import com.ddapps.reservoirreviews.data.remote.models.ResultDataResponse
import com.ddapps.reservoirreviews.domain.common.model.MovieDisplay
import com.squareup.picasso.Picasso
import com.squareup.picasso.RequestCreator

fun List<ResultDataResponse>.mapForRoom(): List<ReviewEntity>{
    val initialList = this
    val entityList = mutableListOf<ReviewEntity>()
    initialList.forEach {
        val reviewEntity = ReviewEntity(
            id = null,
            movie_title = it.display_title,
            movie_img_url = it.multimedia?.src ?: "",
            movie_img_local = "",
            movie_release_date = it.publication_date,
            critics_name = it.byline,
            critics_choise = it.critics_pick,
            critics_short_review = it.summary_short,
            complete_review_link = it.link.url,
            user_favorite = 0)
        entityList.add(reviewEntity)
    }
    return entityList
}


fun List<ReviewEntity>.mapForView(): List<MovieDisplay>{
    val initialList = this
    val displayList = mutableListOf<MovieDisplay>()
    initialList.forEach {
        val movieDislay = MovieDisplay(it.movie_title, it.movie_img_url, it.critics_name)
        displayList.add(movieDislay)
    }
    return displayList
}

@BindingAdapter("imagePath")
fun ImageView.load(imagePath: String?) {
    val imageView = this
//    val requestOptions = Picasso.with(imageView.context).load("file://$imagePath").centerInside()
    if (imagePath.isNullOrEmpty()){
        return
    } else {
        val requestOptions = Picasso.with(imageView.context).load(imagePath)
        requestOptions.into(this)
    }


}

fun View.mudarVisibilidade(){
    if(this.visibility == View.VISIBLE)
        this.visibility = View.INVISIBLE
    else if(this.visibility == View.INVISIBLE)
        this.visibility = View.VISIBLE
}