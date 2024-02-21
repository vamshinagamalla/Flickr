package com.vnagamalla.flickr.repo

/**
 * Created by Vamshi Nagamalla on 2/20/24.
 * Copyright © Vamshi Nagamalla. All rights reserved.
 *
 * API client for the Flickr image search API
 * */
class ImageSearchApiClient(private val api: ImageSearchApi) {

    suspend fun search(query: String) = api.search(query)
}