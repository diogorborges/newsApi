package com.x0.newsapi.data.repository

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import com.x0.newsapi.data.local.NewsApiLocalDataSource
import com.x0.newsapi.data.model.news.Article
import com.x0.newsapi.data.model.news.NewsResponse
import com.x0.newsapi.data.model.sources.Source
import com.x0.newsapi.data.remote.NewsApiRemoteDataSource
import com.x0.newsapi.test.DefaultPluginTestSetup
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Assert
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

class SourcesRepositoryTest : DefaultPluginTestSetup() {

    @InjectMocks
    private lateinit var sourcesRepository: SourcesRepository

    @Mock
    private lateinit var newsApiRemoteDataSource: NewsApiRemoteDataSource

    @Mock
    private lateinit var newsApiLocalDataSource: NewsApiLocalDataSource

    @Mock
    private lateinit var context: Context

    @Before
    fun setup() = MockitoAnnotations.initMocks(this)

    @Test
    fun testGetSourcesFromAPI() {
        val source = Mockito.mock(Source::class.java)
        val emptySourceList = java.util.ArrayList<Source>(1)
        val sourceList = java.util.ArrayList<Source>(1)
        sourceList.add(source)

        val connectivityManager = Mockito.mock(ConnectivityManager::class.java)
        val networkInfo = Mockito.mock(NetworkInfo::class.java)

        `when`(context.getSystemService("connectivity")).thenReturn(connectivityManager)
        `when`(connectivityManager.activeNetworkInfo).thenReturn(networkInfo)
        `when`(networkInfo.isConnected).thenReturn(true)
        `when`(newsApiLocalDataSource.getSources()).thenReturn(Single.just(emptySourceList))
        `when`(newsApiRemoteDataSource.getSources()).thenReturn(Single.just(sourceList))

        val testObserver = TestObserver<ArrayList<Source>>()
        sourcesRepository.getSources().subscribe(testObserver)

        verify(newsApiLocalDataSource).insertSources(source)
    }

    @Test
    fun testGetSourcesNetworkException() {
        val source = Mockito.mock(Source::class.java)
        val emptySourceList = java.util.ArrayList<Source>(1)
        val sourceList = java.util.ArrayList<Source>(1)
        sourceList.add(source)

        val connectivityManager = Mockito.mock(ConnectivityManager::class.java)
        val networkInfo = Mockito.mock(NetworkInfo::class.java)

        `when`(context.getSystemService("connectivity")).thenReturn(connectivityManager)
        `when`(connectivityManager.activeNetworkInfo).thenReturn(networkInfo)
        `when`(networkInfo.isConnected).thenReturn(false)
        `when`(newsApiLocalDataSource.getSources()).thenReturn(Single.just(emptySourceList))
        `when`(newsApiRemoteDataSource.getSources()).thenReturn(Single.just(sourceList))

        val testObserver = TestObserver<ArrayList<Source>>()
        sourcesRepository.getSources().subscribe(testObserver)

        verify(newsApiLocalDataSource, never()).insertSources(source)
    }

    @Test
    fun testGetSourcesFromDB() {
        val source = Mockito.mock(Source::class.java)
        val sourceList = java.util.ArrayList<Source>(1)
        sourceList.add(source)

        `when`(newsApiLocalDataSource.getSources()).thenReturn(Single.just(sourceList))

        val testObserver = TestObserver<ArrayList<Source>>()
        val sources = sourcesRepository.getSources()
        sources.subscribe(testObserver)

        verify(newsApiLocalDataSource, never()).insertSources(source)
    }
}