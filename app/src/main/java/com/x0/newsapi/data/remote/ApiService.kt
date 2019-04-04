package com.x0.newsapi.data.remote

import com.x0.newsapi.data.remote.model.news.NewsResponse
import com.x0.newsapi.data.remote.model.sources.SourcesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    companion object {
        const val GET_SOURCES = "sources"
        const val GET_NEWS = "top-headlines"
    }

    @GET(GET_SOURCES)
    fun getSources(): Single<SourcesResponse>

    @GET(GET_NEWS)
    fun getNews(@Query("sources") sources: String?, @Query("q") subject: String?): Single<NewsResponse>
}
