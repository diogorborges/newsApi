package com.x0.newsapi.data.remote

import com.x0.newsapi.data.model.news.NewsResponse
import com.x0.newsapi.data.model.sources.Source
import io.reactivex.Single
import javax.inject.Inject

class NewsApiRemoteDataSource @Inject constructor(private val newApiService: NewApiService) {

    fun getSources(): Single<ArrayList<Source>> =
        newApiService.getSources().map { it.sources }

    fun getNews(nextPage: Int): Single<NewsResponse> =
        newApiService.getNews(nextPage)

    fun getArticles(sourceId: String, nextPage: Int): Single<NewsResponse> =
        newApiService.getArticles(sourceId, nextPage)
}