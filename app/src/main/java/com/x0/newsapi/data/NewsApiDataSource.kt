package com.x0.newsapi.data

import com.x0.newsapi.data.model.news.Article
import com.x0.newsapi.data.model.news.NewsResponse
import com.x0.newsapi.data.model.sources.Source
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Query

interface NewsApiDataSource {
    fun getFavoriteSources(isFavorite: Boolean): Single<ArrayList<Source>>

    fun getSources(): Single<ArrayList<Source>>

    fun insertSources(vararg sources: Source): Completable

    fun getSourceById(@Query("sources") sources: String): Single<NewsResponse>

    fun insertNews(vararg news: Article): Completable

    fun deleteNews(): Completable
}