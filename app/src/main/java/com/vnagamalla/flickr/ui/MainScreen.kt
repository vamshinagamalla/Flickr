package com.vnagamalla.flickr.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.vnagamalla.flickr.R
import com.vnagamalla.flickr.data.ImageItem
import com.vnagamalla.flickr.ui.theme.FlickrTheme

/**
 * Flickr image search results screen
 * */
@Composable
fun ImageSearchResultsScreen(
    viewModel: MainViewModel = hiltViewModel(),
) {
    var searchText by remember { mutableStateOf(TextFieldValue()) }
    val searchQuery by viewModel.searchQuery.collectAsState()
    val searching by viewModel.searching.observeAsState()
    val imagesState = viewModel.images.collectAsState()

    LaunchedEffect(searchQuery) {
        viewModel.setSearchQuery(searchQuery)
    }

    FlickrTheme {
        Surface {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = searchText,
                    onValueChange = {
                        searchText = it
                        viewModel.setSearchQuery(it.text)
                    },
                    label = { Text(stringResource(id = R.string.label_search)) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .scale(1.0f)
                        .semantics { contentDescription = "Search images" }
                )

                Spacer(modifier = Modifier.height(16.dp))

                if (searching == true) {
                    CircularProgressIndicator(modifier = Modifier
                        .semantics {
                            contentDescription = "Searching images"
                        }
                        .size(50.dp)
                        .align(Alignment.CenterHorizontally))
                } else {
                    LazyVerticalGrid(columns = GridCells.Adaptive(minSize = 150.dp)) {
                        val images = imagesState.value
                        items(images.size) {
                            ImageGridItem(imageItem = images[it]) { imageItem ->
                                viewModel.launchDetailsView.postValue(imageItem)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ImageGridItem(imageItem: ImageItem, navigateToDetails: (ImageItem) -> Unit) {
    Column(
        modifier = Modifier
            .clickable { navigateToDetails(imageItem) } // Make the entire Column clickable
            .border(0.5.dp, color = Color.Gray, RectangleShape)
            .clip(RectangleShape)
            .padding(2.dp)
            .semantics {
                contentDescription = "Click to view details of ${imageItem.title}"
            }
    ) {
        val painter = rememberAsyncImagePainter(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imageItem.imageMedia.imageUrl)
                .size(150.dp.value.toInt(), 150.dp.value.toInt())
                .build()
        )

        Image(
            painter = painter,
            contentDescription = imageItem.title,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .size(150.dp)
        )
        Text(
            text = imageItem.title,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(start = 5.dp, end = 5.dp)
        )
    }
}