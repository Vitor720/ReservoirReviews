package com.ddapps.reservoirreviews.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.ddapps.reservoirreviews.R
import com.ddapps.reservoirreviews.adapters.ReviewsAdapter
import com.ddapps.reservoirreviews.databinding.FragmentHomeBinding
import com.ddapps.reservoirreviews.ui.viewmodel.HomeViewModel
import kotlinx.coroutines.runBlocking
import org.koin.android.ext.android.inject
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ddapps.reservoirreviews.data.local.dao.ReviewDao
import com.ddapps.reservoirreviews.data.repository.MovieRepository
import com.ddapps.reservoirreviews.domain.common.model.MovieDisplay
import com.ddapps.reservoirreviews.domain.common.networking.Resource
import com.ddapps.reservoirreviews.domain.common.networking.Status
import com.ddapps.reservoirreviews.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber

class HomeFragment : Fragment(), IReviewClickListener {
    private var binding: FragmentHomeBinding? = null
    private var adapter: ReviewsAdapter? = null
    private val viewModel: HomeViewModel by viewModel()

    private val observer = Observer<Resource<List<MovieDisplay>>> {
        when (it.status) {
            Status.SUCCESS -> {
                binding?.progress?.hide()
                binding?.emptyReviewLayout?.hide()
                loadRecycler(it.data!!)
            }
            Status.ERROR -> {
                binding?.progress?.hide()
                binding?.emptyReviewLayout?.show()
                Timber.e(it.message.toString() )
            }
            Status.LOADING -> { binding?.progress?.show()}
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding!!.viewModel = viewModel
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getReviewList().observe(viewLifecycleOwner, observer)
        setSearchBar()
    }

    private fun setSearchBar() {
        binding?.searchImage?.setOnClickListener {
            val title = binding?.searchText!!.text.toString()
            adapter = null
            viewModel.loadReviews(title, 0, 0)

        }
    }

    private fun loadRecycler(reviewList: List<MovieDisplay>){
        if (adapter != null) {
            adapter?.addMoreItems(reviewList)
        } else {
            val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            binding?.recyclerMovieReviews?.layoutManager = layoutManager
            adapter = ReviewsAdapter(reviewList, this)
            binding?.recyclerMovieReviews?.adapter = adapter
            setUpScrollListener(layoutManager)
        }
    }


    private fun setUpScrollListener(layoutManager: StaggeredGridLayoutManager) {
        val scrollListener = object : EndlessRecyclerViewScrollListener(layoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int, view: RecyclerView?) {
//                viewModel.loadMovies(title, offset, DEFAULT_LIMIT)
            }
        }
        binding?.recyclerMovieReviews?.addOnScrollListener(scrollListener)
    }

    override fun onClick(movieTitle: String) {
        val navegateDetailsFragment = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(movieTitle)
        adapter = null
        findNavController().navigate(navegateDetailsFragment)
    }
}