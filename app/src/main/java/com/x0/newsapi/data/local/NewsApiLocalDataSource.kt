package com.x0.newsapi.data.local

import com.x0.newsapi.data.LocalDataNotFoundException
import com.x0.newsapi.data.NewsApiDataSource
import com.x0.newsapi.data.model.news.NewsResponse
import com.x0.newsapi.data.model.sources.Source
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class NewsApiLocalDataSource @Inject constructor(val sourcesDao: SourcesDao) : NewsApiDataSource {

    override fun getFavoriteSources(isFavorite: Boolean): Single<ArrayList<Source>> =
        sourcesDao.getFavoriteSources(isFavorite)

    override fun updateFavoriteSource(newsId: String, isFavorite: Boolean): Completable =
        Completable.fromAction { sourcesDao.updateFavoriteSource(newsId, isFavorite) }

    override fun getSources(): Single<ArrayList<Source>> = sourcesDao.getSources()

    override fun insertSources(vararg sources: Source): Completable =
        Completable.fromAction { sourcesDao.insertSources(*sources) }

    override fun deleteSources(): Completable =
        Completable.fromAction { sourcesDao.deleteSources() }

    override fun getSourcesBySubject(subject: String): Single<NewsResponse> =
        Single.error(LocalDataNotFoundException())

    override fun getSourceById(sources: String): Single<NewsResponse> =
        Single.error(LocalDataNotFoundException())
}