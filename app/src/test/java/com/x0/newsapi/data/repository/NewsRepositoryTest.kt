package com.x0.newsapi.data.repository

import com.x0.newsapi.data.local.NewsApiLocalDataSource
import com.x0.newsapi.test.DefaultPluginTestSetup
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class NewsRepositoryTest : DefaultPluginTestSetup() {

    @InjectMocks
    private lateinit var newsRepository: NewsRepository

    @Mock
    private lateinit var newsApiLocalDataSource: NewsApiLocalDataSource

    @Before
    fun setup() = MockitoAnnotations.initMocks(this)

    @Test
    fun shouldLoadMoreNews() {
        `when`(newsRepository.shouldLoadMoreNews()).thenReturn(true)

        newsRepository.shouldLoadMoreNews()

        verify(newsApiLocalDataSource).getShouldLoadMoreNews()
    }

    @Test
    fun getNewsPageNumber() {
        `when`(newsRepository.getNewsPageNumber()).thenReturn(1)

        newsRepository.getNewsPageNumber()

        verify(newsApiLocalDataSource).getNewsPageNumber()
    }

    @Test
    fun saveShouldLoadMoreNews() {
        newsRepository.saveShouldLoadMoreNews(true)

        verify(newsApiLocalDataSource).saveShouldLoadMoreNews(true)
    }
}