package com.vnagamalla.flickr.repo

/**
 * Created by Vamshi Nagamalla on 2/20/24.
 * Copyright © Vamshi Nagamalla. All rights reserved.
 *
 * Repository implementation for the image search API
 * */
class ImageSearchRepoImpl(private val apiClient: ImageSearchApiClient): ImageSearchRepo {

    override suspend fun search(query: String) = apiClient.search(query)
}