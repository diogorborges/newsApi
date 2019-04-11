package com.x0.newsapi.data.usecase

import com.x0.newsapi.data.model.news.Article
import com.x0.newsapi.data.repository.ArticleRepository
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

class ArticleUseCaseTest : DefaultPluginTestSetup() {

    @InjectMocks
    private lateinit var articleUseCase: ArticleUseCase

    @Mock
    private lateinit var articleRepository: ArticleRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun testGetArticlePageNumber() {
        `when`(articleRepository.getArticlesPageNumber()).thenReturn(1)

        articleUseCase.getArticlesPageNumber()

        verify(articleRepository).getArticlesPageNumber()
    }

    @Test
    fun testShouldLoadMoreArticles() {
        `when`(articleRepository.shouldLoadMoreArticles()).thenReturn(true)

        articleUseCase.shouldLoadMoreArticles()

        verify(articleRepository).shouldLoadMoreArticles()
    }

    @Test
    fun testSaveShouldLoadMoreArticles() {
        articleUseCase.saveShouldLoadArticles(true)

        verify(articleRepository).saveShouldLoadMoreArticles(true)
    }

    @Test
    fun testGetArticlesTotalResult() {
        `when`(articleRepository.getArticlesTotalResult()).thenReturn(1)

        articleUseCase.getArticlesTotalResult()

        verify(articleRepository).getArticlesTotalResult()
    }

    @Test
    fun testGetArticlesListSize() {
        `when`(articleRepository.getArticlesListSize()).thenReturn(1)

        articleUseCase.getArticlesListSize()

        verify(articleRepository).getArticlesListSize()
    }

    @Test
    fun testClearArticles() {
        articleUseCase.clearArticles()

        verify(articleRepository).clearArticles()
    }

    @Test
    fun testGetArticles() {
        val article = Mockito.mock(Article::class.java)
        val articleList = java.util.ArrayList<Article>(1)
        articleList.add(article)

        `when`(articleRepository.getArticles("bbb-news", 1)).thenReturn(Single.just(articleList))
        val testObserver = TestObserver<ArrayList<Article>>()

        articleUseCase.getArticles("bbb-news", 1).subscribe(testObserver)

        verify(articleRepository).getArticles("bbb-news", 1)
    }
}
