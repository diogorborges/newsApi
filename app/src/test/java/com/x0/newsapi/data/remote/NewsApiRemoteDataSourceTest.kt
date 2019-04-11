package com.x0.newsapi.data.remote

import com.x0.newsapi.data.model.news.NewsResponse
import com.x0.newsapi.data.model.sources.Source
import com.x0.newsapi.data.model.sources.SourcesResponse
import com.x0.newsapi.test.DefaultPluginTestSetup
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

class NewsApiRemoteDataSourceTest: DefaultPluginTestSetup() {

    @InjectMocks
    private lateinit var newApiRemoteDataSource: NewsApiRemoteDataSource

    @Mock
    private lateinit var newsApiService: NewApiService

    @Before
    fun setup() = MockitoAnnotations.initMocks(this)

    @Test
        fun testGetSources() {
        val sourceResponse = mock(SourcesResponse::class.java)

        `when`(newsApiService.getSources()).thenReturn(Single.just(sourceResponse))
        val testObserver = TestObserver<ArrayList<Source>>()

        newApiRemoteDataSource.getSources().subscribe(testObserver)

        verify(newsApiService).getSources()
    }

    @Test
    fun testGetNews() {
        val newsResponse = mock(NewsResponse::class.java)

        `when`(newsApiService.getNews(1)).thenReturn(Single.just(newsResponse))
        val testObserver = TestObserver<NewsResponse>()

        newApiRemoteDataSource.getNews(1).subscribe(testObserver)

        verify(newsApiService).getNews(1)
    }

    @Test
    fun testGetArticles() {
        val newsResponse = mock(NewsResponse::class.java)

        `when`(newsApiService.getArticles("bbc-news", 1)).thenReturn(Single.just(newsResponse))
        val testObserver = TestObserver<NewsResponse>()

        newApiRemoteDataSource.getArticles("bbc-news", 1).subscribe(testObserver)

        verify(newsApiService).getArticles("bbc-news", 1)
    }
}