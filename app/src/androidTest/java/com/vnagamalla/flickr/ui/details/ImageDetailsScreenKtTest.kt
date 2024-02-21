package com.vnagamalla.flickr.ui.details

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.vnagamalla.flickr.data.ImageItem
import com.vnagamalla.flickr.data.ImageMedia
import com.vnagamalla.flickr.ui.MainActivity
import org.hamcrest.CoreMatchers.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Created by Vamshi Nagamalla on 2/20/24.
 * Copyright © Vamshi Nagamalla. All rights reserved.
 * */
@RunWith(AndroidJUnit4::class)
class ImageDetailsScreenTest {

    @get:Rule
    val composeTestRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setUp() {
        val fakeImageItem = ImageItem(
            author = "John Doe",
            authorId = "123",
            dateTaken = "2022-01-01",
            description = "Sample description",
            link = "https://live.staticflickr.com/65535/53528101189_f87920b18d_m.jpg",
            imageMedia = ImageMedia("https://live.staticflickr.com/65535/53528101189_f87920b18d_m.jpg"),
            published = "2024-01-30T18:38:14Z",
            tags = "tag1, tag2",
            title = "Sample Image"
        )

        composeTestRule.activity.setContent {
            ImageDetailScreen(
                imageItem = fakeImageItem,
                onBack = {}
            )
        }
    }

    @Test
    fun imageDetailsScreen_shouldDisplayImageDetails() {
        // Verify that the Image title is displayed
        composeTestRule.onNodeWithText("Image Details").assertIsDisplayed()

        // Verify that the Image is displayed
        composeTestRule.onNodeWithContentDescription("Image: Sample Image").assertIsDisplayed()

        // Verify that the Share button is displayed
        composeTestRule.onNodeWithContentDescription("Share image").assertIsDisplayed()
    }
}
