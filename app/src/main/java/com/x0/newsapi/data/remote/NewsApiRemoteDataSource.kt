package com.x0.newsapi.data.remote

import com.x0.newsapi.data.NewsApiDataSource
import com.x0.newsapi.data.RemoteDataNotFoundException
import com.x0.newsapi.data.model.news.NewsResponse
import com.x0.newsapi.data.model.sources.Source
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class NewsApiRemoteDataSource @Inject constructor(private val newApiService: NewApiService) :
    NewsApiDataSource {

    override fun getFavoriteSources(isFavorite: Boolean): Single<ArrayList<Source>> =
        Single.error(RemoteDataNotFoundException())

    override fun updateFavoriteSource(newsId: String, isFavorite: Boolean): Completable =
        Completable.complete()

    override fun getSources(): Single<ArrayList<Source>> = newApiService.getSources().map { it.sources }

    override fun insertSources(vararg sources: Source): Completable = Completable.complete()

    override fun deleteSources(): Completable = Completable.complete()

    override fun getSourcesBySubject(subject: String): Single<NewsResponse> =
        newApiService.getSourcesBySubject(subject)

    override fun getSourceById(sources: String): Single<NewsResponse> =
        newApiService.getSourceById(sources)
}

