package com.ddapps.reservoirreviews.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ddapps.reservoirreviews.R
import com.ddapps.reservoirreviews.databinding.RowReviewBinding
import com.ddapps.reservoirreviews.domain.common.model.MovieDisplay
import com.ddapps.reservoirreviews.utils.IReviewClickListener
import com.ddapps.reservoirreviews.utils.load

class ReviewsAdapter(private var reviewsList: List<MovieDisplay>, private val clickListenerI: IReviewClickListener): RecyclerView.Adapter<ReviewsAdapter.ViewHolder>() {

    var reviewMutableList: MutableList<MovieDisplay> = mutableListOf()


    init {
        reviewMutableList.addAll(reviewsList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: RowReviewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.row_review, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val rowItem = reviewMutableList[position]
        holder.bind(rowItem)
    }

    override fun getItemCount(): Int {
        return reviewMutableList.size
    }


    inner class ViewHolder internal constructor(val binding: RowReviewBinding): RecyclerView.ViewHolder(binding.root){
         val rowLayout = binding.reviewRow

        fun bind(item: MovieDisplay){
            binding.movieTitle.text = item.movieTitle
//            binding.movieImage.load(item.movieImage)
        }

        init {
            rowLayout.setOnClickListener {
                val reviewID = reviewMutableList[adapterPosition].reviewID
                clickListenerI.onClick(reviewID)
            }
        }
    }

    fun addMoreItems(newReiewList: List<MovieDisplay>){
        reviewMutableList.addAll(newReiewList)
        notifyDataSetChanged()
    }

}
