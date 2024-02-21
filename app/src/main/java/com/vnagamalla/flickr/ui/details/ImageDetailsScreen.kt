package com.vnagamalla.flickr.ui.details

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.vnagamalla.flickr.R
import com.vnagamalla.flickr.data.ImageItem
import com.vnagamalla.flickr.ui.theme.FlickrTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * Image details screen to show the details of the image
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImageDetailScreen(imageItem: ImageItem?, onBack: () -> Unit) {
    val context = LocalContext.current
    val isDarkMode = isSystemInDarkTheme()

    FlickrTheme {
        imageItem?.let {
            Surface(color = if (isDarkMode) Color.Black else Color.White) {
                Column {
                    TopAppBar(
                        title = {
                            Text(text = stringResource(id = R.string.label_image_details))
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                onBack.invoke()
                            }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = stringResource(id = R.string.navigate_back))
                            }
                        },
                    )
                    Column(
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxSize()
                    ) {
                        Spacer(modifier = Modifier.height(20.dp))
                        val imageSize = imageItem.getImageSize()
                        val painter = rememberAsyncImagePainter(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(imageItem.imageMedia.imageUrl)
                                .size(width = imageSize?.first ?: 200.dp.value.toInt(),
                                    height = imageSize?.second ?: 200.dp.value.toInt())
                                .build()
                        )
                        Image(
                            painter = painter,
                            contentDescription = "Image: ${imageItem.title}",
                            modifier = Modifier
                                .width(imageSize?.first?.dp ?: 200.dp)
                                .height(imageSize?.second?.dp ?: 200.dp)
                                .align(Alignment.CenterHorizontally),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = imageItem.title,
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Gray,
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .semantics {
                                    contentDescription = "Title: ${imageItem.title}"
                                },
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(
                            text = "${stringResource(id = R.string.label_published_on)} ${formatPublishedDate(imageItem.published)}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray,
                            modifier = Modifier
                                .padding(8.dp)
                                .semantics {
                                    contentDescription =
                                        "Published on: ${formatPublishedDate(imageItem.published)}"
                                },
                        )
                        Text(
                            text = "${stringResource(id = R.string.label_author)} ${imageItem.author}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray,
                            modifier = Modifier
                                .padding(8.dp)
                                .semantics {
                                    contentDescription = "Author: ${imageItem.author}"
                                },
                        )
                        Text(
                            text = "${stringResource(id = R.string.label_tags)} ${imageItem.tags}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray,
                            modifier = Modifier
                                .padding(8.dp)
                                .semantics {
                                    contentDescription = "Tags: ${imageItem.tags}"
                                },
                        )
                        Text(
                            text = "${stringResource(id = R.string.label_link)} ${imageItem.link}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray,
                            modifier = Modifier
                                .padding(8.dp)
                                .semantics {
                                    contentDescription = "Link: ${imageItem.link}"
                                },
                        )
                        Text(
                            text = "${stringResource(id = R.string.label_image_size)} ${imageSize?.first} x ${imageSize?.second}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color.Gray,
                            modifier = Modifier
                                .padding(8.dp)
                                .semantics {
                                    contentDescription =
                                        "Image Size: ${imageSize?.first} x ${imageSize?.second}"
                                },
                        )
                        val failedMessage = stringResource(id = R.string.error_failed_to_load_images)
                        IconButton(
                            onClick = {
                                shareImageFromUrl(context, imageItem.imageMedia.imageUrl,
                                    painter, failedMessage)
                            },
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        ) {
                            Icon(Icons.Filled.Share,
                                contentDescription = stringResource(id = R.string.share_image),
                                tint = Color.Gray)
                        }
                    }
                }
            }
        }
    }
}

private fun shareImageFromUrl(
    context: Context,
    imageUrl: String,
    painter: AsyncImagePainter,
    failedMessage: String
) {
    // Load the image asynchronously
    val request = ImageRequest.Builder(context)
        .data(imageUrl)
        .build()

    CoroutineScope(Dispatchers.IO).launch {
        val result = painter.imageLoader.execute(request)

        // If the image is loaded successfully, retrieve the bitmap and share it
        if (result is SuccessResult) {
            val bitmap = result.drawable.toBitmap()

            // Save the bitmap to a temporary file
            val file = saveBitmapToCache(context, bitmap)

            // Share the image file
            shareImageFile(context, file)
        } else {
            // Handle failure to load image
            withContext(Dispatchers.Main) {
                Toast.makeText(context, failedMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }
}

private fun saveBitmapToCache(context: Context, bitmap: Bitmap): File {
    val cacheDir = context.cacheDir
    val imageFile = File.createTempFile("image", ".png", cacheDir)
    val outputStream = FileOutputStream(imageFile)
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    outputStream.flush()
    outputStream.close()
    return imageFile
}

private fun shareImageFile(context: Context, file: File) {
    val uri = FileProvider.getUriForFile(context, context.packageName + ".provider", file)
    val shareIntent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(Intent.EXTRA_STREAM, uri)
        type = "image/png"
        flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    context.startActivity(Intent.createChooser(shareIntent, "Share Image"))
}

private fun formatPublishedDate(dateTimeString: String): String? {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.ENGLISH)
    val outputFormat = SimpleDateFormat("MMM dd yyyy h:mm a", Locale.ENGLISH)

    val date = inputFormat.parse(dateTimeString)
    return if (date == null) {
        null
    } else {
        outputFormat.format(date)
    }
}
