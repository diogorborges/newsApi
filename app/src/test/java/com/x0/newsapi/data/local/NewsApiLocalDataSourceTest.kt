package com.x0.newsapi.data.local

import com.x0.newsapi.data.model.news.Article
import com.x0.newsapi.data.model.sources.Source
import com.x0.newsapi.test.DefaultPluginTestSetup
import io.reactivex.Single
import io.reactivex.observers.TestObserver
import org.junit.Before
import org.junit.Test
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class NewsApiLocalDataSourceTest : DefaultPluginTestSetup() {

    @InjectMocks
    private lateinit var newsApiLocalDataSource: NewsApiLocalDataSource

    @Mock
    private lateinit var newsDao: NewsDao

    @Mock
    private lateinit var sourcesDao: SourcesDao

    @Mock
    private lateinit var paginationNewsRepository: PaginationNewsRepository

    @Mock
    private lateinit var paginationArticlesRepository: PaginationArticlesRepository

    @Before
    fun setup() = MockitoAnnotations.initMocks(this)

    @Test
    fun testGetSources() {
        val source = mock(Source::class.java)
        val sourceList = ArrayList<Source>(1)
        sourceList.add(source)

        `when`(sourcesDao.getSources()).thenReturn(Single.just(sourceList))
        val testObserver = TestObserver<ArrayList<Source>>()

        newsApiLocalDataSource.getSources().subscribe(testObserver)

        verify(sourcesDao).getSources()
    }

    @Test
    fun testInsertSources() {
        val source = mock(Source::class.java)

        newsApiLocalDataSource.insertSources(source).subscribe()

        verify(sourcesDao).insertSources(source)
    }

    @Test
    fun testGetNews() {
        val source = mock(Article::class.java)
        val articleList = ArrayList<Article>(1)
        articleList.add(source)

        `when`(newsDao.getNews()).thenReturn(Single.just(articleList))
        val testObserver = TestObserver<ArrayList<Article>>()

        newsApiLocalDataSource.getNews().subscribe(testObserver)

        verify(newsDao).getNews()
    }

    @Test
    fun testInsertNews() {
        val article = mock(Article::class.java)

        newsApiLocalDataSource.insertNews(article).subscribe()

        verify(newsDao).insertNews(article)
    }

    @Test
    fun testDeleteNews() {
        newsApiLocalDataSource.deleteNews().subscribe()

        verify(newsDao).deleteNews()
    }

    @Test
    fun testGetShouldLoadMoreNews() {
        `when`(paginationNewsRepository.shouldLoadMoreNews()).thenReturn(true)

        newsApiLocalDataSource.getShouldLoadMoreNews()

        verify(paginationNewsRepository).shouldLoadMoreNews()
    }

    @Test
    fun testSaveShouldLoadMoreNews() {
        newsApiLocalDataSource.saveShouldLoadMoreNews(true)

        verify(paginationNewsRepository).putShouldLoadMoreNews(true)
    }

    @Test
    fun testGetNewsPageNumber() {
        `when`(paginationNewsRepository.getNewsPageNumber()).thenReturn(1)

        newsApiLocalDataSource.getNewsPageNumber()

        verify(paginationNewsRepository).getNewsPageNumber()
    }

    @Test
    fun testSaveNewsPageNumber() {
        newsApiLocalDataSource.saveNewsPageNumber(1).subscribe()

        verify(paginationNewsRepository).putNewsPageNumber(1)
    }

    @Test
    fun testSaveNewsTotalResult() {
        newsApiLocalDataSource.saveNewsTotalResult(1)

        verify(paginationNewsRepository).putNewsTotalResults(1)
    }

    @Test
    fun testGetNewsTotalResult() {
        `when`(paginationNewsRepository.getNewsTotalResults()).thenReturn(1)

        newsApiLocalDataSource.getNewsTotalResult()

        verify(paginationNewsRepository).getNewsTotalResults()
    }

    @Test
    fun testGetShouldLoadMoreArticles() {
        `when`(paginationArticlesRepository.shouldLoadMoreArticles()).thenReturn(true)

        newsApiLocalDataSource.getShouldLoadMoreArticles()

        verify(paginationArticlesRepository).shouldLoadMoreArticles()
    }

    @Test
    fun testSaveShouldLoadMoreArticles() {
        newsApiLocalDataSource.saveShouldLoadMoreArticles(true)

        verify(paginationArticlesRepository).putShouldLoadMoreArticles(true)
    }

    @Test
    fun testGetArticlesPageNumber() {
        `when`(paginationArticlesRepository.getArticlesPageNumber()).thenReturn(1)

        newsApiLocalDataSource.getArticlesPageNumber()

        verify(paginationArticlesRepository).getArticlesPageNumber()
    }

    @Test
    fun testSaveArticlesPageNumber() {
        newsApiLocalDataSource.saveArticlesPageNumber(1)

        verify(paginationArticlesRepository).putArticlesPageNumber(1)
    }

    @Test
    fun testSaveArticlesTotalResult() {
        newsApiLocalDataSource.saveArticlesTotalResult(1)

        verify(paginationArticlesRepository).putArticlesTotalResults(1)
    }

    @Test
    fun testGetArticlesTotalResult() {
        `when`(paginationArticlesRepository.getArticlesTotalResults()).thenReturn(1)

        newsApiLocalDataSource.getArticlesTotalResult()

        verify(paginationArticlesRepository).getArticlesTotalResults()
    }

    @Test
    fun testClearArticlesRepository() {
        newsApiLocalDataSource.clearArticlesRepository()

        verify(paginationArticlesRepository).clearArticles()
    }

    @Test
    fun testGetArticlesListSize() {
        `when`(paginationArticlesRepository.getArticlesListSize()).thenReturn(1)

        newsApiLocalDataSource.getArticlesListSize()

        verify(paginationArticlesRepository).getArticlesListSize()
    }

    @Test
    fun testSaveArticlesListSize() {
        newsApiLocalDataSource.saveArticlesListSize(1)

        verify(paginationArticlesRepository).putArticleListSize(1)
    }
}