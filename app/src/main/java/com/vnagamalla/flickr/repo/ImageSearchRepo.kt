package com.vnagamalla.flickr.repo

import com.vnagamalla.flickr.data.ImageResponse

/**
 * Created by Vamshi Nagamalla on 2/20/24.
 * Copyright © Vamshi Nagamalla. All rights reserved.
 *
 * Repository for the image search API
 * */
interface ImageSearchRepo {

    suspend fun search(query: String): ImageResponse
}