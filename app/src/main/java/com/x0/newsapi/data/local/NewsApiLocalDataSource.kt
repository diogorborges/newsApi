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

    override fun getFavoriteSources(isFavorite: Boolean): Single<ArrayList<Source>> =
        sourcesDao.getFavoriteSources(isFavorite).map { ArrayList(it) }

    override fun updateFavoriteSource(newsId: String, isFavorite: Boolean): Completable =
        Completable.fromAction { sourcesDao.updateFavoriteSource(newsId, isFavorite) }

    override fun getSources(): Single<ArrayList<Source>> =
        sourcesDao.getSources().map { ArrayList(it) }

    override fun insertSources(vararg sources: Source): Completable =
        Completable.fromAction { sourcesDao.insertSources(*sources) }

    override fun deleteSources(): Completable =
        Completable.fromAction { sourcesDao.deleteSources() }

    fun getNews(pageNumber: Int): Single<ArrayList<Article>> =
        newsDao.getNews(pageNumber).map {
            ArrayList(it)
        }

    override fun getSourceById(sources: String): Single<NewsResponse> =
        Single.error(LocalDataNotFoundException())

    override fun insertNews(vararg news: Article): Completable =
        Completable.fromAction { newsDao.insertNews(*news) }

    override fun deleteNews(): Completable = Completable.fromAction { newsDao.deleteNews() }

    fun isFirstLoad(): Boolean = paginationRepository.isFirstLoad()

    fun saveIsFirstLoad(isFirstLoad: Boolean) {
        paginationRepository.putFirstLoad(isFirstLoad)
    }

    fun getPageNumber(): Int = paginationRepository.getPageNumber()

    fun updatePageNumber(nextPage: Int, previousPage: Int): Completable =
        Completable.fromAction {
            newsDao.updatePageNumber(nextPage, previousPage)
        }

    fun savePageNumber(nextPage: Int) {
        paginationRepository.putPageNumber(nextPage)
    }
}