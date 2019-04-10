package com.x0.newsapi

import com.google.gson.Gson
import com.x0.newsapi.common.JsonUtil
import com.x0.newsapi.data.model.sources.Source
import com.x0.newsapi.data.model.sources.SourcesResponse
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

class SourcesResponseJsonTest {

    lateinit var sourcesResponse: SourcesResponse

    lateinit var source: Source

    @Before
    @Throws(IOException::class)
    fun setup() {
        val loadedJsonFromResource = JsonUtil.loadJsonFromResource("sources_response.json")

        sourcesResponse =
            Gson().fromJson<SourcesResponse>(loadedJsonFromResource, SourcesResponse::class.java)

        source = sourcesResponse.sources[0]
    }

    @Test
    fun testSourcesResponseIsNotNull() {
        checkNotNull(sourcesResponse)
    }

    @Test
    fun testStatusIsNotNull() {
        checkNotNull(sourcesResponse.status)
    }

    @Test
    fun testStatus() {
        assertEquals("ok", sourcesResponse.status)
    }

    @Test
    fun testSourcesIsNotNull() {
        checkNotNull(sourcesResponse.sources)
    }

    @Test
    fun testSource() {
        assertEquals("abc-news", source.id)
        assertEquals("ABC News", source.name)
        assertEquals(
            "Your trusted source for breaking news, analysis, exclusive interviews, headlines, and videos at ABCNews.com.",
            source.description
        )
        assertEquals(
            "https://abcnews.go.com",
            source.url
        )
        assertEquals(
            "general",
            source.category
        )
        assertEquals("en", source.language)
        assertEquals("us", source.country)
    }

}
