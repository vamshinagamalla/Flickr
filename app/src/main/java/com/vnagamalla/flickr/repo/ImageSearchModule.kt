package com.vnagamalla.flickr.repo

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

/**
 * Created by Vamshi Nagamalla on 2/20/24.
 * Copyright © Vamshi Nagamalla. All rights reserved.
 *
 * Module for providing the ImageSearch related dependencies
 *
 * Using SingletonComponent in Dagger Hilt is beneficial when we want to ensure that a single
 * instance of a dependency is shared across the entire application and has a long-lived scope.
 * */
@Module
@InstallIn(SingletonComponent::class)
class ImageSearchModule {

    @Provides
    @Singleton
    fun provideImageSearchRepo(apiClient: ImageSearchApiClient): ImageSearchRepo =
        ImageSearchRepoImpl(apiClient)

    @Provides
    @Singleton
    fun provideImageSearchApiClient(api: ImageSearchApi): ImageSearchApiClient =
        ImageSearchApiClient(api)

    @Provides
    @Singleton
    fun provideImageSearchApi(retrofit: Retrofit): ImageSearchApi =
        retrofit.create(ImageSearchApi::class.java)
}