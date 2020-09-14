package com.ddapps.reservoirreviews.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.ddapps.reservoirreviews.R
import com.ddapps.reservoirreviews.adapters.ReviewsAdapter
import com.ddapps.reservoirreviews.databinding.FragmentHomeBinding
import com.ddapps.reservoirreviews.domain.common.model.MovieDisplay
import com.ddapps.reservoirreviews.domain.common.networking.Resource
import com.ddapps.reservoirreviews.domain.common.networking.Status
import com.ddapps.reservoirreviews.ui.viewmodel.HomeViewModel
import com.ddapps.reservoirreviews.utils.EndlessRecyclerViewScrollListener
import com.ddapps.reservoirreviews.utils.IReviewClickListener
import com.ddapps.reservoirreviews.utils.hide
import com.ddapps.reservoirreviews.utils.show
import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), IReviewClickListener {
    private var binding: FragmentHomeBinding? = null
    private var adapter: ReviewsAdapter? = null
    private val viewModel: HomeViewModel by viewModel()

    private val observer = Observer<Resource<List<MovieDisplay>>> {
        when (it.status) {
            Status.SUCCESS -> {
                binding?.progress?.hide()
                if (!it.data.isNullOrEmpty()){
                    binding?.emptyReviewLayout?.hide()
                    loadRecycler(it.data)
                }
            }
            Status.ERROR -> {
                binding?.progress?.hide()
                binding?.emptyReviewLayout?.show()
                binding?.errorText?.text = it.message
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
        viewModel.loadLocalReviews()
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
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        if (adapter != null) {
            adapter?.addMoreItems(reviewList)
        } else {
            binding?.recyclerMovieReviews?.layoutManager = layoutManager
            adapter = ReviewsAdapter(reviewList, this)
            setUpScrollListener(layoutManager)
        }
        binding?.recyclerMovieReviews?.layoutManager = layoutManager
        binding?.recyclerMovieReviews?.adapter = AlphaInAnimationAdapter(adapter!!).apply {
                setDuration(800)
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