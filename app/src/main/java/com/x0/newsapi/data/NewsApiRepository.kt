package com.x0.newsapi.data

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.x0.newsapi.common.ListUtils
import com.x0.newsapi.common.hasNetwork
import com.x0.newsapi.data.local.NewsApiLocalDataSource
import com.x0.newsapi.data.model.news.Article
import com.x0.newsapi.data.model.sources.Source
import com.x0.newsapi.data.model.types.StatusType
import com.x0.newsapi.data.remote.NewsApiRemoteDataSource
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NewsApiRepository @Inject constructor(
    private val newsApiRemoteDataSource: NewsApiRemoteDataSource,
    private val newsApiLocalDataSource: NewsApiLocalDataSource,
    private val context: Context
) : NewsApiDataSource {

    companion object {
        private const val TAG = "NewsApiRepository"
        private const val LIST_IS_EMPTY = 0
        private const val FIRST_PAGE = 1
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
                    .doOnError { Single.just(FailureException()) }
            }
            else -> Single.error(NetworkException())
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

    override fun getSources(): Single<ArrayList<Source>> =
        newsApiLocalDataSource.getSources()
            .flatMap {
                if (it.isNotEmpty()) {
                    Log.i(TAG, "Dispatching ${it.size} sources from DB...")
                    return@flatMap Single.just(it)
                } else {
                    return@flatMap fetchAndPersistSources()
                }
            }

    @SuppressLint("CheckResult")
    private fun fetchAndPersistSources(): Single<ArrayList<Source>> =
        when (hasNetwork(context)) {
            true -> {
                newsApiRemoteDataSource.getSources()
                    .doOnSuccess {
                        Log.i(TAG, "Dispatching ${it.size} sources from API...")
                        persistSources(it)
                    }
                    .doOnError {
                        Single.just(FailureException())
                    }
            }
            false -> Single.error(NetworkException())
        }

    @SuppressLint("CheckResult")
    private fun persistSources(sources: ArrayList<Source>) {
        insertSources(*ListUtils.toArray(Source::class.java, sources))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(
                { Log.i(TAG, "Success persisting sources...") },
                { Log.e(TAG, "Failure persisting sources...") })
    }

    override fun insertSources(vararg sources: Source): Completable =
        newsApiLocalDataSource.insertSources(*sources)

    private fun mergeNews(
        currentNews: ArrayList<Article>,
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
                            else -> Single.error(NetworkException())
                        }
                    }
                    .doAfterSuccess {
                        newsApiLocalDataSource.saveNewsTotalResult(it.totalResults)
                    }
                    .map { it.articles }
                    .map { nextNews ->
                        nextNews.forEach {
                            currentNews.add(it)
                        }
                        currentNews
                    }
                    .doOnSuccess {
                        Log.i(TAG, "Merging ${it.size} new from API/DB...")
                        persistNews(it, nextPage)
                    }
                    .doOnError {
                        Single.just(FailureException())
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
                            else -> Single.error(NetworkException())
                        }
                    }
                    .doAfterSuccess {
                        newsApiLocalDataSource.saveNewsTotalResult(it.totalResults)
                    }
                    .map { it.articles }
                    .doOnSuccess {
                        Log.i(TAG, "Dispatching ${it.size} news from API...")
                        persistNews(it, nextPage)
                    }
                    .doOnError {
                        Single.just(FailureException())
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

    override fun insertNews(vararg news: Article): Completable =
        newsApiLocalDataSource.insertNews(*news)

    override fun deleteNews(): Completable = newsApiLocalDataSource.deleteNews()
        .doOnError { Log.e(TAG, "Failure deleting news...") }

    //

    fun shouldLoadMoreNews(): Boolean = newsApiLocalDataSource.getShouldLoadMoreNews()

    fun getNewsPageNumber(): Int = newsApiLocalDataSource.getNewsPageNumber()

    fun saveShouldLoadMoreNews(shouldLoadMoreNews: Boolean) =
        newsApiLocalDataSource.saveShouldLoadMoreNews(shouldLoadMoreNews)

    //

    fun shouldLoadMoreArticles(): Boolean = newsApiLocalDataSource.getShouldLoadMoreArticles()

    fun getArticlesPageNumber(): Int = newsApiLocalDataSource.getArticlesPageNumber()

    fun saveShouldLoadMoreArticles(shouldLoadMoreArticles: Boolean) =
        newsApiLocalDataSource.saveShouldLoadMoreArticles(shouldLoadMoreArticles)

    fun getArticlesTotalResult(): Int = newsApiLocalDataSource.getArticlesTotalResult()

    fun getArticlesListSize(): Int = newsApiLocalDataSource.getArticlesListSize()

    fun clearArticles() = newsApiLocalDataSource.clearArticlesRepository()
}