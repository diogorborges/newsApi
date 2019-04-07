package com.x0.newsapi.data.remote

import com.x0.newsapi.data.NewsApiDataSource
import com.x0.newsapi.data.RemoteDataNotFoundException
import com.x0.newsapi.data.model.news.Article
import com.x0.newsapi.data.model.news.NewsResponse
import com.x0.newsapi.data.model.sources.Source
import io.reactivex.Completable
import io.reactivex.Single
import javax.inject.Inject

class NewsApiRemoteDataSource @Inject constructor(private val newApiService: NewApiService) :
    NewsApiDataSource {

    override fun getFavoriteSources(isFavorite: Boolean): Single<ArrayList<Source>> =
        Single.error(RemoteDataNotFoundException())

    override fun getSources(): Single<ArrayList<Source>> =
        newApiService.getSources().map { it.sources }

    override fun insertSources(vararg sources: Source): Completable = Completable.complete()

    fun getNews(nextPage: Int): Single<NewsResponse> =
        newApiService.getNews(nextPage)

    override fun getSourceById(sources: String): Single<NewsResponse> =
        newApiService.getSourceById(sources)

    override fun insertNews(vararg news: Article): Completable = Completable.complete()

    override fun deleteNews(): Completable = Completable.complete()
}

