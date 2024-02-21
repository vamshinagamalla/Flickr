package com.vnagamalla.flickr.ui.details

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.DialogFragment
import com.vnagamalla.flickr.data.IMAGE_ITEM_PARCEL_CONSTANT
import com.vnagamalla.flickr.data.ImageItem

/**
 * Created by Vamshi Nagamalla on 2/20/24.
 * Copyright © Vamshi Nagamalla. All rights reserved.
 * */

/**
 * Helper function to create an instance of [ImageDetailsFragment]
 * */
fun imageDetailsFragment(imageItem: ImageItem?): ImageDetailsFragment {
    return ImageDetailsFragment().apply {
        arguments = Bundle()
            .apply {
                putParcelable(IMAGE_ITEM_PARCEL_CONSTANT, imageItem)
            }
    }
}

/**
 * Image details fragment to show the details of the image
 * */
class ImageDetailsFragment: DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar_Fullscreen)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                val imageItem = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    arguments?.getParcelable(IMAGE_ITEM_PARCEL_CONSTANT, ImageItem::class.java)
                } else {
                    arguments?.getParcelable(IMAGE_ITEM_PARCEL_CONSTANT)
                }
                ImageDetailScreen(imageItem,
                    onBack = { dismiss() })
            }
        }
    }
}