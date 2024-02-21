package com.vnagamalla.flickr.data


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

const val IMAGE_ITEM_PARCEL_CONSTANT = "image_item"

/**
 * Data class representing an image item from the Flickr API
 * */
@Parcelize
@JsonClass(generateAdapter = true)
data class ImageItem(
    @Json(name = "author")
    val author: String,
    @Json(name = "author_id")
    val authorId: String,
    @Json(name = "date_taken")
    val dateTaken: String,
    @Json(name = "description")
    val description: String,
    @Json(name = "link")
    val link: String,
    @Json(name = "media")
    val imageMedia: ImageMedia,
    @Json(name = "published")
    val published: String,
    @Json(name = "tags")
    val tags: String,
    @Json(name = "title")
    val title: String
): Parcelable {

    /**
     * Helper function to get the size of the image from the description
     * */
    fun getImageSize(): Pair<Int, Int>? {
        // Regular expression pattern to match width and height attributes
        val pattern = Regex("width=\"(\\d+)\"\\s+height=\"(\\d+)\"")

        // Find the first match in the description
        val matchResult = pattern.find(description)

        // Extract width and height values from the match result
        return matchResult?.let { result ->
            val (widthStr, heightStr) = result.destructured
            Pair(widthStr.toInt(), heightStr.toInt())
        }
    }
}