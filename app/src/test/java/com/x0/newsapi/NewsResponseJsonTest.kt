package com.x0.newsapi

import com.google.gson.Gson
import com.x0.newsapi.common.JsonUtil
import com.x0.newsapi.data.model.news.Article
import com.x0.newsapi.data.model.news.NewsResponse
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.IOException

class NewsResponseJsonTest {

    lateinit var newsResponse: NewsResponse

    lateinit var article: Article

    @Before
    @Throws(IOException::class)
    fun setup() {
        val loadedJsonFromResource = JsonUtil.loadJsonFromResource("news_response.json")

        newsResponse =
            Gson().fromJson<NewsResponse>(loadedJsonFromResource, NewsResponse::class.java)

        article = newsResponse.articles[0]
    }

    @Test
    fun testNewsResponseIsNotNull() {
        checkNotNull(newsResponse)
    }

    @Test
    fun testStatusIsNotNull() {
        checkNotNull(newsResponse.status)
    }

    @Test
    fun testStatus() {
        assertEquals("ok", newsResponse.status)
    }

    @Test
    fun testTotalResultsIsNotNull() {
        checkNotNull(newsResponse.totalResults)
    }

    @Test
    fun testTotalResults() {
        assertEquals(835, newsResponse.totalResults)
    }

    @Test
    fun testArticlesIsNotNull() {
        checkNotNull(newsResponse.articles)
    }

    @Test
    fun testArticleSourceIsNotNull() {
        checkNotNull(article.source)
    }

    @Test
    fun testArticleSource() {
        assertEquals("wired", article.source.id)
        assertEquals("Wired", article.source.name)
    }

    @Test
    fun testArticle() {
        assertEquals("Michael Hardy", article.author)
        assertEquals("Photographing All 2,000 Miles of the US-Mexico Border", article.title)
        assertEquals(
            "America's border communities are politically and culturally diverse, but they agree on one thing: A border wall is a terrible idea.",
            article.description
        )
        assertEquals(
            "https://www.wired.com/story/mexico-us-border-wall-photo-gallery/",
            article.url
        )
        assertEquals(
            "https://media.wired.com/photos/5cabb6ab4e9bfc631a59198b/191:100/pass/01_ElliotRoss_AmericanBackyard.jpg",
            article.urlToImage
        )
        assertEquals("2019-04-09T15:56:00Z", article.publishedAt)
    }
}
