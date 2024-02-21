package com.vnagamalla.flickr.ui

import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import com.squareup.moshi.Moshi
import com.vnagamalla.flickr.R
import com.vnagamalla.flickr.ui.details.ImageDetailsFragment
import com.vnagamalla.flickr.ui.details.imageDetailsFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Created by Vamshi Nagamalla on 2/20/24.
 * Copyright © Vamshi Nagamalla. All rights reserved.
 *
 * Main activity for the app
 * */
@AndroidEntryPoint
class MainActivity : FragmentActivity() {

    private val viewModel: MainViewModel by viewModels()

    @Inject
    lateinit var moshi: Moshi

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ImageSearchResultsScreen()
        }

        viewModel.launchDetailsView.observe(this) {
            imageDetailsFragment(it).show(supportFragmentManager, ImageDetailsFragment::javaClass.name)
        }

        viewModel.errorLoadingImage.observe(this) {
            Toast.makeText(
                this,
                getString(R.string.error_failed_to_load_images),
                Toast.LENGTH_SHORT
            ).show()
        }
    }
}