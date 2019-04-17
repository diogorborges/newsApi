package com.x0.newsapi.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.x0.newsapi.common.ListUtils
import com.x0.newsapi.common.hasNetwork
import com.x0.newsapi.data.FailureException
import com.x0.newsapi.data.NetworkException
import com.x0.newsapi.data.local.NewsApiLocalDataSource
import com.x0.newsapi.data.model.news.Article
import com.x0.newsapi.data.model.types.StatusType
import com.x0.newsapi.data.remote.NewsApiRemoteDataSource
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NewsRepository @Inject constructor(
    private val newsApiRemoteDataSource: NewsApiRemoteDataSource,
    private val newsApiLocalDataSource: NewsApiLocalDataSource,
    private val context: Context
) {

    companion object {
        private const val TAG = "NewsRepository"
        private const val LIST_IS_EMPTY = 0
        private const val FIRST_PAGE = 1
    }

    fun getNews(pageNumber: Int, isRefreshing: Boolean): Single<ArrayList<Article>> =
        when (isRefreshing) {
            true -> fetchAndPersistNews(FIRST_PAGE)
            else -> checkPersistedNews(pageNumber)
        }

    private fun checkPersistedNews(pageNumber: Int): Single<ArrayList<Article>> =
        newsApiLocalDataSource.getNews()
            .flatMap {
                val newsListSize = it.size
                when (newsListSize) {
                    LIST_IS_EMPTY -> return@flatMap fetchAndPersistNews(pageNumber)
                    else -> {
                        if (newsListSize == newsApiLocalDataSource.getNewsTotalResult()) {
                            return@flatMap Single.just(it)
                        } else {
                            return@flatMap mergeNews(it, pageNumber)
                        }
                    }
                }
            }

    private fun mergeNews(
        currentNews: ArrayList<Article>,
        nextPage: Int
    ): Single<ArrayList<Article>> =
        when (hasNetwork(context)) {
            true -> {
                newsApiRemoteDataSource.getNews(nextPage)
                    .flatMap {
                        when (it.status) {
                            StatusType.STATUS_OK.toLowerCase() -> {
                                Single.just(it)
                            }
                            else -> Single.error(NetworkException())
                        }
                    }
                    .doAfterSuccess {
                        newsApiLocalDataSource.saveNewsTotalResult(it.totalResults)
                    }
                    .map { it.articles }
                    .map { nextNews ->
                        currentNews.addAll(nextNews)
                        currentNews
                    }
                    .doOnSuccess {
                        Log.i(TAG, "Removing old new from DB...")
                        deleteNews()
                    }
                    .doAfterSuccess {
                        Log.i(TAG, "Merging ${it.size} new from API/DB...")
                        persistNews(it, nextPage)
                    }
                    .doOnError {
                        Log.i(TAG, "Error ${it.message}")
                    }
            }
            else -> Single.just(currentNews)
        }

    private fun fetchAndPersistNews(
        nextPage: Int
    ): Single<ArrayList<Article>> =
        when (hasNetwork(context)) {
            true -> {
                deleteNews().andThen(newsApiRemoteDataSource.getNews(nextPage))
                    .flatMap {
                        when (it.status) {
                            StatusType.STATUS_OK.toLowerCase() -> {
                                Single.just(it)
                            }
                            else -> Single.error(FailureException())
                        }
                    }
                    .doAfterSuccess {
                        newsApiLocalDataSource.saveNewsTotalResult(it.totalResults)
                    }
                    .map { it.articles }
                    .doOnSuccess {
                        Log.i(TAG, "Dispatching ${it.size} new from API...")
                        persistNews(it, nextPage)
                    }
                    .doOnError {
                        Log.i(TAG, "Error ${it.message}")
                    }
            }
            else -> Single.error(NetworkException())
        }

    @SuppressLint("CheckResult")
    private fun persistNews(
        news: ArrayList<Article>,
        nextPage: Int
    ) {
        insertNews(*ListUtils.toArray(Article::class.java, news))
            .andThen(newsApiLocalDataSource.saveNewsPageNumber(nextPage))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { Log.i(TAG, "Success persisting news...") },
                { Log.e(TAG, "Failure persisting news...") })
    }

    private fun insertNews(vararg news: Article): Completable =
        newsApiLocalDataSource.insertNews(*news)

    private fun deleteNews(): Completable = newsApiLocalDataSource.deleteNews()
        .doOnError { Log.e(TAG, "Failure deleting news...") }

    fun shouldLoadMoreNews(): Boolean = newsApiLocalDataSource.getShouldLoadMoreNews()

    fun getNewsPageNumber(): Int = newsApiLocalDataSource.getNewsPageNumber()

    fun saveShouldLoadMoreNews(shouldLoadMoreNews: Boolean) =
        newsApiLocalDataSource.saveShouldLoadMoreNews(shouldLoadMoreNews)
}