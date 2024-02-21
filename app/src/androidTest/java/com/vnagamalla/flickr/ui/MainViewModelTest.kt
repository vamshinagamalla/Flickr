package com.vnagamalla.flickr.ui

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.vnagamalla.flickr.repo.ImageSearchRepo
import kotlinx.coroutines.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy

class MainViewModelTest {

    // Executes tasks in the Architecture Components in the same thread
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repo: ImageSearchRepo
    private lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        repo = mock()
        viewModel = spy(MainViewModel(repo))
    }

    @Test
    fun setSearchQueryUpdatesSearchQuery() {
        // Given
        val query = "cats"

        // When
        viewModel.setSearchQuery(query)

        // Then
        assertThat(viewModel.searchQuery.value).isEqualTo(query)
    }
}
