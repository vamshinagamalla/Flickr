package com.vnagamalla.flickr.data

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Data class representing the response from the Flickr API
 * */
@JsonClass(generateAdapter = true)
data class ImageResponse(
    @Json(name = "description")
    val description: String,
    @Json(name = "generator")
    val generator: String,
    @Json(name = "items")
    val imageItems: List<ImageItem>,
    @Json(name = "link")
    val link: String,
    @Json(name = "modified")
    val modified: String,
    @Json(name = "title")
    val title: String
)