package com.x0.newsapi.data.local

import com.x0.newsapi.data.model.news.Article
import com.x0.newsapi.data.model.sources.Source
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class NewsApiLocalDataSource @Inject constructor(
    private val sourcesDao: SourcesDao,
    private val newsDao: NewsDao,
    private val paginationNewsRepository: PaginationNewsRepository,
    private val paginationArticlesRepository: PaginationArticlesRepository
)  {

    fun getSources(): Single<ArrayList<Source>> =
        sourcesDao.getSources().map { ArrayList(it) }

    fun insertSources(vararg sources: Source): Completable =
        Completable.fromAction { sourcesDao.insertSources(*sources) }

    //

    fun getNews(): Single<ArrayList<Article>> = newsDao.getNews().map { ArrayList(it) }

    fun insertNews(vararg news: Article): Completable =
        Completable.fromAction { newsDao.insertNews(*news) }

    fun deleteNews(): Completable = Completable.fromAction { newsDao.deleteNews() }

    ///

    fun getShouldLoadMoreNews(): Boolean = paginationNewsRepository.shouldLoadMoreNews()

    fun saveShouldLoadMoreNews(shouldLoadMore: Boolean) =
        paginationNewsRepository.putShouldLoadMoreNews(shouldLoadMore)

    fun getNewsPageNumber(): Int = paginationNewsRepository.getNewsPageNumber()

    fun saveNewsPageNumber(nextPage: Int): Completable =
        Completable.fromAction { paginationNewsRepository.putNewsPageNumber(nextPage) }

    fun saveNewsTotalResult(newsTotalResults: Int) =
        paginationNewsRepository.putNewsTotalResults(newsTotalResults)

    fun getNewsTotalResult() = paginationNewsRepository.getNewsTotalResults()

    //

    fun getShouldLoadMoreArticles(): Boolean = paginationArticlesRepository.shouldLoadMoreArticles()

    fun saveShouldLoadMoreArticles(shouldLoadMore: Boolean) =
        paginationArticlesRepository.putShouldLoadMoreArticles(shouldLoadMore)

    fun getArticlesPageNumber(): Int = paginationArticlesRepository.getArticlesPageNumber()

    fun saveArticlesPageNumber(nextPage: Int) =
        paginationArticlesRepository.putArticlesPageNumber(nextPage)

    fun saveArticlesTotalResult(articlesTotalResults: Int) =
        paginationArticlesRepository.putArticlesTotalResults(articlesTotalResults)

    fun getArticlesTotalResult() = paginationArticlesRepository.getArticlesTotalResults()

    fun clearArticlesRepository() = paginationArticlesRepository.clearArticles()

    fun getArticlesListSize() = paginationArticlesRepository.getArticlesListSize()

    fun saveArticlesListSize(articleListSize: Int) =
        paginationArticlesRepository.putArticleListSize(articleListSize)
}