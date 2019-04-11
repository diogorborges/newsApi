package com.x0.newsapi.presentation.news

import com.x0.newsapi.data.model.news.Article
import com.x0.newsapi.data.usecase.NewsUseCase
import com.x0.newsapi.test.DefaultPluginTestSetup
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations
import java.lang.Exception

class NewsPresenterTest : DefaultPluginTestSetup() {

    @InjectMocks
    private lateinit var newsPresenter: NewsPresenter

    @Mock
    private lateinit var view: NewsFragment

    @Mock
    private lateinit var newsUseCase: NewsUseCase

    @Before
    fun setup() = MockitoAnnotations.initMocks(this)

    @Test
    fun testGetNews() {
        val article = mock(Article::class.java)
        val articleList = ArrayList<Article>(1)
        articleList.add(article)

        `when`(newsUseCase.getNewsPageNumber()).thenReturn(1)
        `when`(newsUseCase.getNews(1, false)).thenReturn(Single.just(articleList))

        newsPresenter.setView(view)

        verify(view).showLoader(true)
        verify(view).showNews(anyList())
        verify(view).showLoader(false)
    }

    @Test
    fun testGetNewsError() {
        val article = mock(Article::class.java)
        val articleList = ArrayList<Article>(1)
        articleList.add(article)

        `when`(newsUseCase.getNewsPageNumber()).thenReturn(1)
        `when`(newsUseCase.getNews(1, false)).thenReturn(Single.error(Exception()))

        newsPresenter.setView(view)

        verify(view).showLoader(true)
        verify(view).showError(null)
        verify(view).showLoader(false)
    }
}