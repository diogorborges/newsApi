package com.x0.newsapi.data.remote

import com.x0.newsapi.data.model.news.NewsResponse
import com.x0.newsapi.data.model.sources.SourcesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface NewApiService {

    companion object {
        const val GET_SOURCES = "sources"
        const val GET_NEWS = "top-headlines"
    }

    @GET(GET_SOURCES)
    fun getSources(): Single<SourcesResponse>

    @GET(GET_NEWS)
    fun getArticles(@Query("sources") source: String, @Query("page") page: Int): Single<NewsResponse>

    @GET(GET_NEWS)
    fun getNews(@Query("page") page: Int, @Query("language") language: String = "en"): Single<NewsResponse>
}
