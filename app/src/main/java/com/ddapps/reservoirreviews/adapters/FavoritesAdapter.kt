package com.ddapps.reservoirreviews.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.ddapps.reservoirreviews.R
import com.ddapps.reservoirreviews.databinding.RowFavoriteBinding
import com.ddapps.reservoirreviews.domain.common.model.MovieDisplay
import com.ddapps.reservoirreviews.ui.fragments.FavoritesFragment
import com.ddapps.reservoirreviews.ui.fragments.FavoritesFragmentDirections
import com.ddapps.reservoirreviews.ui.fragments.HomeFragmentDirections
import com.ddapps.reservoirreviews.utils.IReviewClickListener
import com.ddapps.reservoirreviews.utils.SwipeItemTouchHelper
import kotlinx.android.synthetic.main.item_swipe_undo.view.*
import timber.log.Timber
import java.util.*

class FavoritesAdapter(private val context: Context, private val list: List<MovieDisplay>, private val clickListener: IReviewClickListener): RecyclerView.Adapter<RecyclerView.ViewHolder?>(), SwipeItemTouchHelper.SwipeHelperAdapter {
    private var items = mutableListOf<MovieDisplay>()

    init {
        items.addAll(list)
    }
    private val itemsSwiped: MutableList<MovieDisplay> = ArrayList<MovieDisplay>()

    inner class OriginalViewHolder internal constructor(val binding: RowFavoriteBinding) : RecyclerView.ViewHolder(binding.root), SwipeItemTouchHelper.TouchViewHolder {
        fun bind(item: MovieDisplay){
            binding.movie = item
        }
        override fun onItemSelected() {
            itemView.setBackgroundColor(context.resources.getColor(R.color.grey_10))
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }

        init {
            binding.rowLayout.setOnClickListener {
                val navegateDetailsFragment = FavoritesFragmentDirections.actionFavoritesFragmentToDetailsFragment(items[adapterPosition].movieTitle)
                binding.rowLayout.findNavController().navigate(navegateDetailsFragment)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: RowFavoriteBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context),
            R.layout.row_favorite,
            parent,
            false)
        return OriginalViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is OriginalViewHolder) {
            val row = items[position]
            holder.bind(row)

            holder.binding.swipeLayout.bt_undo.setOnClickListener {
                items[position].swiped = false
                itemsSwiped.remove(items[position])
                notifyItemChanged(position)
            }
            if (row.swiped) {
                holder.binding.rowLayout.visibility = View.GONE
            } else {
                holder.binding.rowLayout.visibility = View.VISIBLE
            }
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                for (s in itemsSwiped) {
                    clickListener.onClick(s.movieTitle)
                    val indexRemoved = items.indexOf(s)
                    if (indexRemoved != -1) {
                        items.removeAt(indexRemoved)
                        notifyItemRemoved(indexRemoved)
                    }
                }
                itemsSwiped.clear()
                super.onScrollStateChanged(recyclerView, newState)
            }
        })
        super.onAttachedToRecyclerView(recyclerView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onItemDismiss(position: Int) {
        //Remove automaticamente com swipe duplo
        if (items[position].swiped) {
            itemsSwiped.remove(items[position])
            clickListener.onClick(items[position].movieTitle)
            items.removeAt(position)
            notifyItemRemoved(position)
            return
        }
        //Marca para remoção
        items[position].swiped = true
        itemsSwiped.add(items[position])
        notifyItemChanged(position)
    }
}