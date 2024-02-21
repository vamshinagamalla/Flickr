package com.vnagamalla.flickr.repo

import com.vnagamalla.flickr.data.ImageResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Created by Vamshi Nagamalla on 2/20/24.
 * Copyright © Vamshi Nagamalla. All rights reserved.
 *
 * Retrofit interface for the Flickr image search API, if there are any other API's we can add them here.
 * */
interface ImageSearchApi {

    @GET("/services/feeds/photos_public.gne?format=json&nojsoncallback=1")
    suspend fun search(@Query("tags") query: String): ImageResponse
}