package com.ddapps.reservoirreviews.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.ddapps.reservoirreviews.R
import com.ddapps.reservoirreviews.databinding.FragmentDetailsBinding
import com.ddapps.reservoirreviews.domain.common.model.MovieDisplay
import com.ddapps.reservoirreviews.domain.common.networking.Resource
import com.ddapps.reservoirreviews.domain.common.networking.Status
import com.ddapps.reservoirreviews.ui.viewmodel.DetailsViewModel
import com.ddapps.reservoirreviews.ui.viewmodel.HomeViewModel
import com.ddapps.reservoirreviews.utils.mudarVisibilidade
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class DetailsFragment : Fragment() {

    private var binding: FragmentDetailsBinding? = null
    private val viewModel: DetailsViewModel by viewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = DetailsFragmentArgs.fromBundle(requireArguments())
        val movieTitle = args.title
        viewModel.getSingleReviewByTitle(movieTitle)
        viewModel.getMovieReview().observe(viewLifecycleOwner, observer)
    }

    private val observer = Observer<Resource<MovieDisplay>> {
        when (it.status) {
            Status.SUCCESS -> {
                binding?.movie = it.data
                binding?.executePendingBindings()
            }
            Status.ERROR -> {
                Timber.e(it.message.toString() )
            }
            Status.LOADING -> {}
        }
    }

}