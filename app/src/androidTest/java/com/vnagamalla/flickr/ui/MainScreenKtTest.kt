package com.vnagamalla.flickr.ui

import androidx.activity.compose.setContent
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.vnagamalla.flickr.data.ImageItem
import com.vnagamalla.flickr.data.ImageMedia
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.verify
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@ExperimentalComposeUiApi
@HiltAndroidTest
class ImageSearchResultsScreenTest {

    @get:Rule
    var composeTestRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun imageSearchResultsScreen_Searching() {
        // Given
        val viewModel = FakeMainViewModel().apply {
            setSearchQuery("query")
            _searching.postValue(true)
        }
        composeTestRule.activity.setContent {
            ImageSearchResultsScreen(viewModel)
        }

        // Then
        composeTestRule.onNodeWithContentDescription("Searching images").assertIsDisplayed()
    }

    @Test
    fun imageSearchResultsScreen_ShowImages() {
        // Given
        val imageItem = ImageItem(
            author = "Author",
            authorId = "123",
            dateTaken = "2022-02-20",
            description = "Description",
            link = "https://live.staticflickr.com/65535/53528101189_f87920b18d_m.jpg",
            imageMedia = ImageMedia("https://live.staticflickr.com/65535/53528101189_f87920b18d_m.jpg"),
            published = "2024-01-30T18:38:14Z",
            tags = "tag1, tag2",
            title = "Title"
        )
        val viewModel = FakeMainViewModel().apply {
            setSearchQuery("query")
            _searching.postValue(false)
            _images.value = listOf(imageItem)
        }
        composeTestRule.activity.setContent {
            ImageSearchResultsScreen(viewModel)
        }

        // Then
        composeTestRule.onNodeWithContentDescription("Click to view details of ${imageItem.title}").assertExists()
    }

    @Test
    fun imageGridItem_Clickable() {
        // Given
        val imageItem = ImageItem(
            author = "Author",
            authorId = "123",
            dateTaken = "2022-02-20",
            description = "Description",
            link = "https://live.staticflickr.com/65535/53528101189_f87920b18d_m.jpg",
            imageMedia = ImageMedia("https://live.staticflickr.com/65535/53528101189_f87920b18d_m.jpg"),
            published = "2024-01-30T18:38:14Z",
            tags = "tag1, tag2",
            title = "Title"
        )
        val navigateToDetails: (ImageItem) -> Unit = mock()
        composeTestRule.activity.setContent {
            ImageGridItem(imageItem, navigateToDetails)
        }

        // Then
        composeTestRule.onNodeWithContentDescription("Click to view details of ${imageItem.title}")
            .performClick()
        verify(navigateToDetails).invoke(imageItem)
    }
}

class FakeMainViewModel : MainViewModel(mock()) {
    private val _searchQuery = MutableStateFlow("")
    override val searchQuery: StateFlow<String> = _searchQuery

    val _images = MutableStateFlow<List<ImageItem>>(emptyList())
    override val images: StateFlow<List<ImageItem>> = _images

    val _searching = MutableLiveData<Boolean>()
    override val searching: LiveData<Boolean> = _searching

    override fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
}
