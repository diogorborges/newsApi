package com.x0.newsapi.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.x0.newsapi.data.local.NewsApiLocalDataSource
import com.x0.newsapi.data.model.news.Article
import com.x0.newsapi.data.model.news.NewsResponse
import com.x0.newsapi.data.remote.NewsApiRemoteDataSource
import com.x0.newsapi.test.DefaultPluginTestSetup
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.never
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class ArticleRepositoryTest : DefaultPluginTestSetup() {

    @InjectMocks
    private lateinit var articleRepository: ArticleRepository

    @Mock
    private lateinit var newsApiRemoteDataSource: NewsApiRemoteDataSource

    @Mock
    private lateinit var newsApiLocalDataSource: NewsApiLocalDataSource

    @Mock
    private lateinit var context: Context

    private lateinit var networkInfo: NetworkInfo

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        val connectivityManager = Mockito.mock(ConnectivityManager::class.java)
        networkInfo = Mockito.mock(NetworkInfo::class.java)

        `when`(context.getSystemService("connectivity")).thenReturn(connectivityManager)
        `when`(connectivityManager.activeNetworkInfo).thenReturn(networkInfo)
    }

    @Test
    fun getArticles() {
        val newsResponse = Mockito.mock(NewsResponse::class.java)

        `when`(networkInfo.isConnected).thenReturn(true)
        `when`(newsApiRemoteDataSource.getArticles("bbc-news", 1)).thenReturn(
            Single.just(
                newsResponse
            )
        )
        `when`(newsResponse.status).thenReturn("ok")

        val testObserver = TestObserver<ArrayList<Article>>()
        articleRepository.getArticles("bbc-news", 1).subscribe(testObserver)

        verify(newsApiLocalDataSource).saveArticlesListSize(ArgumentMatchers.anyInt())
        verify(newsApiLocalDataSource).saveArticlesPageNumber(ArgumentMatchers.anyInt())
        verify(newsApiLocalDataSource).saveArticlesTotalResult(ArgumentMatchers.anyInt())
    }

    @Test
    fun getArticlesNetworkException() {
        `when`(networkInfo.isConnected).thenReturn(false)

        val testObserver = TestObserver<ArrayList<Article>>()
        articleRepository.getArticles("bbc-news", 1).subscribe(testObserver)

        verify(newsApiLocalDataSource, never()).saveArticlesListSize(ArgumentMatchers.anyInt())
        verify(newsApiLocalDataSource, never()).saveArticlesPageNumber(ArgumentMatchers.anyInt())
        verify(newsApiLocalDataSource, never()).saveArticlesTotalResult(ArgumentMatchers.anyInt())
    }

    @Test
    fun shouldLoadMoreArticles() {
        `when`(articleRepository.shouldLoadMoreArticles()).thenReturn(true)

        articleRepository.shouldLoadMoreArticles()

        verify(newsApiLocalDataSource).getShouldLoadMoreArticles()
    }

    @Test
    fun getArticlesPageNumber() {
        `when`(articleRepository.getArticlesPageNumber()).thenReturn(1)

        articleRepository.getArticlesPageNumber()

        verify(newsApiLocalDataSource).getArticlesPageNumber()
    }

    @Test
    fun saveShouldLoadMoreArticles() {
        articleRepository.saveShouldLoadMoreArticles(true)

        verify(newsApiLocalDataSource).saveShouldLoadMoreArticles(true)
    }

    @Test
    fun getArticlesTotalResult() {
        `when`(articleRepository.getArticlesTotalResult()).thenReturn(1)

        articleRepository.getArticlesTotalResult()

        verify(newsApiLocalDataSource).getArticlesTotalResult()
    }

    @Test
    fun getArticlesListSize() {
        `when`(articleRepository.getArticlesTotalResult()).thenReturn(1)

        articleRepository.getArticlesTotalResult()

        verify(newsApiLocalDataSource).getArticlesTotalResult()
    }

    @Test
    fun clearArticles() {
        articleRepository.clearArticles()

        verify(newsApiLocalDataSource).clearArticlesRepository()
    }
}