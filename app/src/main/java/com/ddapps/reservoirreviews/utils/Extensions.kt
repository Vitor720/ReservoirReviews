package com.ddapps.reservoirreviews.utils

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.ddapps.reservoirreviews.data.local.entity.ReviewEntity
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
            movie_title = it.display_title ?: "Sem Titulo",
            movie_img_url = it.multimedia?.src ?: "",
            movie_img_local = "",
            movie_release_date = it.opening_date ?: "Data não fornecida",
            critics_name = it.byline ?: "Sem autor",
            critics_choise = it.critics_pick ?: 0,
            critics_short_review = it.summary_short ?: "Resumo não encontrado",
            complete_review_link = it.link?.url ?: "",
            user_favorite = 0)
        entityList.add(reviewEntity)
    }
    return entityList
}

fun ReviewEntity.mapForView(): MovieDisplay{
    val reviewEntity = this
    return MovieDisplay( movieTitle  = reviewEntity.movie_title,
                         movieImage  = reviewEntity.movie_img_url,
                         releaseDate = reviewEntity.movie_release_date,
                         shortReview = reviewEntity.critics_short_review,
                         reviewLink  = reviewEntity.complete_review_link,
                         criticsName = reviewEntity.critics_name,
                         criticsPick = reviewEntity.critics_choise == CRITICS_APPROVAL)

}

fun List<ReviewEntity>.mapForView(): List<MovieDisplay>{
    val entityList = this
    return entityList.map { it.mapForView() }
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

@set:BindingAdapter("isVisible")
inline var View.isVisible: Boolean
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

fun View.show() {
    this.visibility = View.VISIBLE
}

fun View.hide() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}
