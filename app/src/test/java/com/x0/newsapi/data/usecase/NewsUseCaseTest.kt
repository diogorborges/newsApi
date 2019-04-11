package com.x0.newsapi.data.usecase

import com.x0.newsapi.data.model.news.Article
import com.x0.newsapi.data.repository.NewsRepository
import com.x0.newsapi.test.DefaultPluginTestSetup
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class NewsUseCaseTest : DefaultPluginTestSetup() {

    @InjectMocks
    private lateinit var newsUseCase: NewsUseCase

    @Mock
    private lateinit var newsRepository: NewsRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testGetNewsPageNumber() {
        `when`(newsRepository.getNewsPageNumber()).thenReturn(1)

        newsUseCase.getNewsPageNumber()

        verify(newsRepository).getNewsPageNumber()
    }

    @Test
    fun testShouldLoadMoreNews() {
        `when`(newsRepository.shouldLoadMoreNews()).thenReturn(true)

        newsUseCase.shouldLoadMoreNews()

        verify(newsRepository).shouldLoadMoreNews()
    }

    @Test
    fun testSaveShouldLoadMoreNews() {
        newsUseCase.saveShouldLoadMoreNews(true)

        verify(newsRepository).saveShouldLoadMoreNews(true)
    }

    @Test
    fun testGetNews() {
        val article = Mockito.mock(Article::class.java)
        val articleList = java.util.ArrayList<Article>(1)
        articleList.add(article)

        `when`(newsRepository.getNews(1, true)).thenReturn(Single.just(articleList))
        val testObserver = TestObserver<ArrayList<Article>>()

        newsUseCase.getNews(1, true).subscribe(testObserver)

        verify(newsRepository).getNews(1, true)
    }
}