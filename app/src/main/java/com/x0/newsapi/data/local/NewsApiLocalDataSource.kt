package com.x0.newsapi.data.local

import com.x0.newsapi.data.LocalDataNotFoundException
import com.x0.newsapi.data.NewsApiDataSource
import com.x0.newsapi.data.model.news.Article
import com.x0.newsapi.data.model.news.NewsResponse
import com.x0.newsapi.data.model.sources.Source
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class NewsApiLocalDataSource @Inject constructor(
    private val sourcesDao: SourcesDao,
    private val newsDao: NewsDao,
    private val paginationRepository: PaginationRepository
) : NewsApiDataSource {

    override fun getSources(): Single<ArrayList<Source>> =
        sourcesDao.getSources().map { ArrayList(it) }

    override fun insertSources(vararg sources: Source): Completable =
        Completable.fromAction { sourcesDao.insertSources(*sources) }

    fun getNews(): Single<ArrayList<Article>> = newsDao.getNews().map { ArrayList(it) }

    override fun getSourceById(sources: String): Single<NewsResponse> =
        Single.error(LocalDataNotFoundException())

    override fun insertNews(vararg news: Article): Completable =
        Completable.fromAction { newsDao.insertNews(*news) }

    override fun deleteNews(): Completable = Completable.fromAction { newsDao.deleteNews() }

    fun getShouldLoadMore(): Boolean = paginationRepository.shouldLoadMore()

    fun saveShouldLoadMore(shouldLoadMore: Boolean) = paginationRepository.putShouldLoadMore(shouldLoadMore)

    fun getPageNumber(): Int = paginationRepository.getPageNumber()

    fun savePageNumber(nextPage: Int): Completable =
        Completable.fromAction { paginationRepository.putPageNumber(nextPage) }
}