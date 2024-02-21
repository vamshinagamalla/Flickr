package com.vnagamalla.flickr.data

import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class ImageItemTest {

    @Test
    fun testWithValidWidthAndHeight() {
        // Given
        val description = "Description with width=\"100\" height=\"200\""
        val imageItem = ImageItem(author = "", authorId = "", dateTaken = "", description = description,
            link = "", imageMedia = ImageMedia(""), published = "", tags = "", title = "")

        // When
        val imageSize = imageItem.getImageSize()

        // Then
        Assertions.assertEquals(Pair(100, 200), imageSize)
    }

    @Test
    fun testImageSizeWithoutValidDimensions() {
        // Given
        val description = "Description without valid attributes"
        val imageItem = ImageItem(author = "", authorId = "", dateTaken = "", description = description,
            link = "", imageMedia = ImageMedia(""), published = "", tags = "", title = "")

        // When
        val imageSize = imageItem.getImageSize()

        // Then
        Assertions.assertEquals(null, imageSize)
    }

    @Test
    fun testImageSizeWhenDescriptionIsNull() {
        // Given
        val imageItem = ImageItem(author = "", authorId = "", dateTaken = "", description = "",
            link = "", imageMedia = ImageMedia(""), published = "", tags = "", title = "")

        // When
        val imageSize = imageItem.getImageSize()

        // Then
        Assertions.assertEquals(null, imageSize)
    }

    @Test
    fun testImageSizeWhenDescriptionIsEmpty() {
        // Given
        val imageItem = ImageItem(author = "", authorId = "", dateTaken = "", description = "",
            link = "", imageMedia = ImageMedia(""), published = "", tags = "", title = "")

        // When
        val imageSize = imageItem.getImageSize()

        // Then
        Assertions.assertEquals(null, imageSize)
    }
}