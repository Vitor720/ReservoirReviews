package com.ddapps.reservoirreviews.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.ddapps.reservoirreviews.R
import com.ddapps.reservoirreviews.databinding.FragmentDetailsBinding
import com.ddapps.reservoirreviews.domain.common.model.MovieDisplay
import com.ddapps.reservoirreviews.domain.common.networking.Resource
import com.ddapps.reservoirreviews.domain.common.networking.Status
import com.ddapps.reservoirreviews.ui.viewmodel.DetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import timber.log.Timber


class DetailsFragment : Fragment() {

    private var binding: FragmentDetailsBinding? = null
    private val viewModel: DetailsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_details, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args = DetailsFragmentArgs.fromBundle(requireArguments())
        val movieTitle = args.title
        viewModel.getSingleReviewByTitle(movieTitle)
        viewModel.getMovieReview().observe(viewLifecycleOwner, observer)
        setFavoriteButton()
    }


    private fun setFavoriteButton(){
        binding?.favoriteImg?.setOnClickListener {
            viewModel.setCurrentReviewFavorite()
        }
    }

    private val observer = Observer<Resource<MovieDisplay>> {
        when (it.status) {
            Status.SUCCESS -> {
                binding?.movie = it.data
                binding?.executePendingBindings()
                binding?.reviewLink?.setOnClickListener { _ ->
                    loadFullReview(binding?.movie?.reviewLink)
                }
                binding?.shareImg?.setOnClickListener { _ ->
                    shareReview(it.data?.movieTitle ?: "", it.data?.reviewLink ?: "")
                }
            }
            Status.ERROR -> {
                Timber.e(it.message.toString())
            }
            Status.LOADING -> {
            }
        }
    }

    private fun loadFullReview(link: String?) {
        try {
            val webView = binding!!.webView
            webView.webViewClient = WebViewClient()
            webView.settings.javaScriptEnabled = true
            webView.loadUrl(link!!)
            binding?.reviewLink?.text = "Full Review"
            binding?.reviewLink?.setTextAppearance(R.style.TextAppearance_AppCompat_Title)
        } catch (t: Throwable){
            Toast.makeText(requireContext(), "Check your internet and try again", Toast.LENGTH_LONG).show()
        }
    }

    private fun shareReview(movieTitle: String, link: String){
        val share = Intent(Intent.ACTION_SEND)
        share.type = "text/plain"
        share.putExtra(Intent.EXTRA_SUBJECT, movieTitle)
        share.putExtra(Intent.EXTRA_TEXT, link)
        startActivity(Intent.createChooser(share, "$movieTitle Review!"))
    }

}