package com.x0.newsapi.data

import android.annotation.SuppressLint
import android.content.Context
import android.text.TextUtils.isEmpty
import android.util.Log
import com.x0.newsapi.common.ListUtils
import com.x0.newsapi.common.hasNetwork
import com.x0.newsapi.data.local.NewsApiLocalDataSource
import com.x0.newsapi.data.model.news.Article
import com.x0.newsapi.data.model.news.NewsResponse
import com.x0.newsapi.data.model.sources.Source
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
        private const val IS_EMPTY = 0
        private const val MAX_LIMIT = 100
    }

    fun getNews(nextPage: Int): Single<ArrayList<Article>> =
        newsApiLocalDataSource.getNews()
            .flatMap {
                when(it.size) {
                    IS_EMPTY -> return@flatMap fetchAndPersistNews(nextPage)
                    MAX_LIMIT -> return@flatMap Single.just(it)
                    else -> return@flatMap mergeNews(it, nextPage)
                }
            }

    override fun getFavoriteSources(isFavorite: Boolean): Single<ArrayList<Source>> =
        newsApiLocalDataSource.getFavoriteSources(isFavorite)

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
        if (hasNetwork(context)) {
            newsApiRemoteDataSource.getSources()
                .doOnSuccess {
                    Log.i(TAG, "Dispatching ${it.size} sources from API...")
                    persistSources(it)
                }
                .doOnError {
                    Single.just(FailureException())
                }
        } else {
            Single.error(NetworkException())
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
    ): Single<ArrayList<Article>> {
        return if (hasNetwork(context)) {
            deleteNews().andThen(newsApiRemoteDataSource.getNews(nextPage))
                .map { it.articles }
                .map { nextNews ->
                    currentNews.forEach {
                        nextNews.add(it)
                    }
                    nextNews
                }
                .doOnSuccess {
                    Log.i(TAG, "Merging ${it.size} new from API/DB...")
                    persistNews(it, nextPage)
                }
                .doOnError {
                    Single.just(FailureException())
                }
        } else {
            Single.just(currentNews)
        }
    }

    private fun fetchAndPersistNews(
        nextPage: Int
    ): Single<ArrayList<Article>> =
        newsApiRemoteDataSource.getNews(nextPage)
            .map { it.articles }
            .doOnSuccess {
                Log.i(TAG, "Dispatching ${it.size} news from API...")
                persistNews(it, nextPage)
            }
            .doOnError {
                Single.just(FailureException())
            }

    @SuppressLint("CheckResult")
    private fun persistNews(
        news: ArrayList<Article>,
        nextPage: Int
    ) {
        insertNews(*ListUtils.toArray(Article::class.java, news))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnComplete { newsApiLocalDataSource.savePageNumber(nextPage) }
            .subscribe(
                { Log.i(TAG, "Success persisting news...") },
                { Log.e(TAG, "Failure persisting news...") })
    }

    override fun insertNews(vararg news: Article): Completable =
        newsApiLocalDataSource.insertNews(*news)

    override fun deleteNews(): Completable = newsApiLocalDataSource.deleteNews()
        .doOnError { Log.e(TAG, "Failure deleting news...") }

    override fun getSourceById(sources: String): Single<NewsResponse> =
        newsApiRemoteDataSource.getSourceById(sources)

    fun isFirstLoad(): Boolean = newsApiLocalDataSource.isFirstLoad()

    fun getPageNumber(): Int = newsApiLocalDataSource.getPageNumber()

    fun saveIsFirstLoad(firstLoad: Boolean) = newsApiLocalDataSource.saveIsFirstLoad(firstLoad)
}