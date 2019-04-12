package com.x0.newsapi.presentation.sourcelist

import com.x0.newsapi.data.model.news.Article
import com.x0.newsapi.data.usecase.ArticleUseCase
import com.x0.newsapi.test.DefaultPluginTestSetup
import io.reactivex.Single
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyList
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class ArticleListPresenterTest : DefaultPluginTestSetup() {

    @InjectMocks
    private lateinit var articleListPresenter: ArticleListPresenter

    @Mock
    private lateinit var view: ArticleListFragment

    @Mock
    private lateinit var articleUseCase: ArticleUseCase

    @Before
    fun setup() = MockitoAnnotations.initMocks(this)

    @Test
    fun testGetArticles() {
        val article = Mockito.mock(Article::class.java)
        val articleList = ArrayList<Article>(1)
        articleList.add(article)

        `when`(articleUseCase.getArticlesPageNumber()).thenReturn(1)
        `when`(articleUseCase.getArticlesListSize()).thenReturn(1)
        `when`(articleUseCase.getArticlesTotalResult()).thenReturn(2)
        `when`(articleUseCase.getArticles("bbc-news", 1)).thenReturn(Single.just(articleList))

        articleListPresenter.setView(view, "bbc-news")

        verify(view).showLoader(true)
        verify(view).showArticleList(anyList())
        verify(view).showLoader(false)
    }

    @Test
    fun testGetArticlesError() {
        val article = Mockito.mock(Article::class.java)
        val articleList = ArrayList<Article>(1)
        articleList.add(article)

        `when`(articleUseCase.getArticlesPageNumber()).thenReturn(1)
        `when`(articleUseCase.getArticlesListSize()).thenReturn(1)
        `when`(articleUseCase.getArticlesTotalResult()).thenReturn(2)
        `when`(articleUseCase.getArticles("bbc-news", 1)).thenReturn(Single.error(Exception()))

        articleListPresenter.setView(view, "bbc-news")

        verify(view).showLoader(true)
        verify(view).showError(null)
        verify(view).showLoader(false)
    }

    @Test
    fun testHasReachedTotalResults() {
        val article = Mockito.mock(Article::class.java)
        val articleList = ArrayList<Article>(1)
        articleList.add(article)

        `when`(articleUseCase.getArticlesPageNumber()).thenReturn(1)
        `when`(articleUseCase.getArticlesListSize()).thenReturn(1)
        `when`(articleUseCase.getArticlesTotalResult()).thenReturn(1)

        articleListPresenter.setView(view, "bbc-news")

        verify(view).showRefreshing(false)
    }

    @Test
    fun clearArticles() {
        articleListPresenter.clearArticles()

        verify(articleUseCase).clearArticles()
    }
}