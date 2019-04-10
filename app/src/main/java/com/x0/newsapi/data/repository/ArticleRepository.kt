package com.x0.newsapi.data.repository

import android.content.Context
import android.util.Log
import com.x0.newsapi.common.hasNetwork
import com.x0.newsapi.data.NetworkException
import com.x0.newsapi.data.local.NewsApiLocalDataSource
import com.x0.newsapi.data.model.news.Article
import com.x0.newsapi.data.model.types.StatusType
import com.x0.newsapi.data.remote.NewsApiRemoteDataSource
import io.reactivex.Single
import javax.inject.Inject

class ArticleRepository @Inject constructor(
    private val newsApiRemoteDataSource: NewsApiRemoteDataSource,
    private val newsApiLocalDataSource: NewsApiLocalDataSource,
    private val context: Context
) {

    companion object {
        private const val TAG = "ArticleRepository"
    }

    fun getArticles(sourceId: String, nextPage: Int): Single<ArrayList<Article>> =
        when (hasNetwork(context)) {
            true -> {
                newsApiRemoteDataSource.getArticles(sourceId, nextPage)
                    .flatMap {
                        when (it.status) {
                            StatusType.STATUS_OK.toLowerCase() -> Single.just(it)
                            else -> Single.error(NetworkException())
                        }
                    }
                    .doAfterSuccess { newsApiLocalDataSource.saveArticlesTotalResult(it.totalResults) }
                    .map { it.articles }
                    .doOnSuccess {
                        Log.i(TAG, "Dispatching ${it.size} sources from API...")
                        newsApiLocalDataSource.saveArticlesListSize(it.size)
                        newsApiLocalDataSource.saveArticlesPageNumber(nextPage)
                    }
                    .doOnError {
                        Log.i(TAG, "Error ${it.message}")
                    }
            }
            else -> Single.error(NetworkException())
        }

    fun shouldLoadMoreArticles(): Boolean = newsApiLocalDataSource.getShouldLoadMoreArticles()

    fun getArticlesPageNumber(): Int = newsApiLocalDataSource.getArticlesPageNumber()

    fun saveShouldLoadMoreArticles(shouldLoadMoreArticles: Boolean) =
        newsApiLocalDataSource.saveShouldLoadMoreArticles(shouldLoadMoreArticles)

    fun getArticlesTotalResult(): Int = newsApiLocalDataSource.getArticlesTotalResult()

    fun getArticlesListSize(): Int = newsApiLocalDataSource.getArticlesListSize()

    fun clearArticles() = newsApiLocalDataSource.clearArticlesRepository()
}