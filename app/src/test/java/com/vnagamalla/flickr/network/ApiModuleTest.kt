package com.vnagamalla.flickr.network

import android.content.Context
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.io.File

@RunWith(JUnit4::class)
class ApiModuleTest {

    @Mock
    private lateinit var mockContext: Context

    lateinit var apiModule: ApiModule

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        Mockito.`when`(mockContext.cacheDir).thenReturn(File("/cacheDir")) // Adjust as needed
        apiModule = ApiModule()
    }

    @Test
    fun testProvideRetrofit() {
        val httpClient = apiModule.provideHttpClient(
            apiModule.provideCache(mockContext),
            apiModule.provideLoggingInterceptor(),
        )
        val retrofit = apiModule.provideRetrofit(
            httpClient,
            apiModule.provideMoshi()
        )
        Assert.assertNotNull(retrofit)
    }

    @Test
    fun testProvideLoggingInterceptor() {
        val interceptor = apiModule.provideLoggingInterceptor()
        Assert.assertNotNull(interceptor)
    }

    @Test
    fun testProvideHttpClient() {
        val client = apiModule.provideHttpClient(
            apiModule.provideCache(mockContext),
            apiModule.provideLoggingInterceptor(),
        )
        Assert.assertNotNull(client)
    }

    @Test
    fun testProvideCache() {
        val cache = apiModule.provideCache(mockContext)
        Assert.assertNotNull(cache)
    }

    @Test
    fun testProvideMoshi() {
        val moshi = apiModule.provideMoshi()
        Assert.assertNotNull(moshi)
    }
}
