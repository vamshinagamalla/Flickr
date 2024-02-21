package com.vnagamalla.flickr.data


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

/**
 * Data class representing the media object in the Flickr API
 * */
@Parcelize
@JsonClass(generateAdapter = true)
data class ImageMedia(
    @Json(name = "m")
    val imageUrl: String
): Parcelable