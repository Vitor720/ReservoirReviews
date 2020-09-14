package com.ddapps.reservoirreviews.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.ddapps.reservoirreviews.R
import com.ddapps.reservoirreviews.adapters.FavoritesAdapter
import com.ddapps.reservoirreviews.databinding.FragmentFavoritesBinding
import com.ddapps.reservoirreviews.domain.common.model.MovieDisplay
import com.ddapps.reservoirreviews.domain.common.networking.Resource
import com.ddapps.reservoirreviews.domain.common.networking.Status
import com.ddapps.reservoirreviews.ui.viewmodel.FavoriteViewModel
import com.ddapps.reservoirreviews.utils.IReviewClickListener
import com.ddapps.reservoirreviews.utils.SwipeItemTouchHelper
import com.ddapps.reservoirreviews.utils.hide
import com.ddapps.reservoirreviews.utils.show
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class FavoritesFragment : Fragment(), IReviewClickListener {

    private var binding: FragmentFavoritesBinding? = null
    private lateinit var adapter: FavoritesAdapter
    private val viewModel: FavoriteViewModel by viewModel()
    private var itemTouchHelper: ItemTouchHelper? = null

    private val observer = Observer<Resource<List<MovieDisplay>>> {
        when (it.status) {
            Status.SUCCESS -> {
                if (it.data.isNullOrEmpty()) {
                    binding?.emptyReviewLayout?.show()
                } else {
                    binding?.emptyReviewLayout?.hide()
                    loadRecycler(it.data)
                }
            }
            Status.ERROR -> {
                binding?.emptyReviewLayout?.show()
                Timber.e(it.message.toString())
            }
            Status.LOADING -> {
                binding?.emptyReviewLayout?.show()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_favorites, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getFavoritesList().observe(viewLifecycleOwner, observer)
        viewModel.getAllFavoriteRevies()
    }

    private fun loadRecycler(reviewList: List<MovieDisplay>){
        val layoutManager = LinearLayoutManager(requireContext())
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding?.recyclerFavorites?.layoutManager = layoutManager
        adapter = FavoritesAdapter(requireContext(), reviewList)
        binding!!.recyclerFavorites.adapter = adapter

        val callback: ItemTouchHelper.Callback = SwipeItemTouchHelper(adapter)
        itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper?.attachToRecyclerView(binding?.recyclerFavorites)
    }

    override fun onClick(movieTitle: String) {
    }
}


