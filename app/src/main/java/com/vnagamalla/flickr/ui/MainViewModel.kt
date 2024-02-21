package com.vnagamalla.flickr.ui

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vnagamalla.flickr.data.ImageItem
import com.vnagamalla.flickr.repo.ImageSearchRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.debounce
import javax.inject.Inject

/**
 * Created by Vamshi Nagamalla on 2/20/24.
 * Copyright © Vamshi Nagamalla. All rights reserved.
 *
 * View model for the main screen
 * */
@HiltViewModel
open class MainViewModel @Inject constructor(private val repo: ImageSearchRepo) : ViewModel() {
    private val _searchQuery = MutableStateFlow("porcupine")//default to porcupine
    open val searchQuery: StateFlow<String> = _searchQuery

    private val _images = MutableStateFlow<List<ImageItem>>(emptyList())
    open val images: StateFlow<List<ImageItem>> = _images

    private val _searching = MutableLiveData<Boolean>()
    open val searching: LiveData<Boolean> = _searching

    val launchDetailsView = SingleLiveEvent<ImageItem?>()
    val errorLoadingImage = SingleLiveEvent<Void>()

    init {
        observeSearchQuery()
    }

    @OptIn(FlowPreview::class)
    private fun observeSearchQuery() {
        viewModelScope.launch {
            _searchQuery
                .debounce(300)
                .collectLatest { query ->
                    searchImages(query)
                }
        }
    }

    open fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    private fun searchImages(query: String) {
        _searching.value = true
        viewModelScope.launch(Dispatchers.IO) {
            try {
                _images.value = repo.search(query).imageItems
            } catch (e: Exception) {
                errorLoadingImage.postCall()
            } finally {
                _searching.postValue(false)
            }
        }
    }
}