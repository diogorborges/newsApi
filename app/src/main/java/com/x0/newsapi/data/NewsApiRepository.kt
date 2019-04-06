package com.x0.newsapi.data

import android.annotation.SuppressLint
import android.util.Log
import com.x0.newsapi.common.ListUtils
import com.x0.newsapi.data.local.NewsApiLocalDataSource
import com.x0.newsapi.data.model.news.Article
import com.x0.newsapi.data.model.news.NewsResponse
import com.x0.newsapi.data.model.sources.Source
import com.x0.newsapi.data.remote.NewsApiRemoteDataSource
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NewsApiRepository @Inject constructor(
    private val newsApiRemoteDataSource: NewsApiRemoteDataSource,
    private val newsApiLocalDataSource: NewsApiLocalDataSource
) : NewsApiDataSource {

    companion object {
        const val TAG = "NewsApiRepository"
    }

    override fun getFavoriteSources(isFavorite: Boolean): Single<ArrayList<Source>> =
        newsApiLocalDataSource.getFavoriteSources(isFavorite)

    override fun updateFavoriteSource(newsId: String, isFavorite: Boolean): Completable =
        newsApiLocalDataSource.updateFavoriteSource(newsId, isFavorite)

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

    private fun fetchAndPersistSources(): Single<ArrayList<Source>> =
        newsApiRemoteDataSource.getSources()
            .doOnSuccess {
                Log.i(TAG, "Dispatching ${it.size} sources from API...")
                persistSources(it)
            }
            .doOnError {
                Single.just(arrayListOf(Source))
            }

    @SuppressLint("CheckResult")
    private fun persistSources(sources: ArrayList<Source>) {
        insertSources(*ListUtils.toArray(Source::class.java, sources))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(
                { Log.i(TAG, "Success persisting sources...") },
                { Log.e(TAG, "Failure persisting sources...") })
    }

    override fun insertSources(vararg sources: Source): Completable =
        newsApiLocalDataSource.insertSources(*sources)

    override fun deleteSources(): Completable = newsApiLocalDataSource.deleteSources()
        .doOnError { Log.e(TAG, "Failure deleting sources...") }

    fun getNews(nextPage: Int): Single<ArrayList<Article>> {
        val previousPage = nextPage.minus(1)
        return newsApiLocalDataSource.getNews(previousPage)
            .flatMap {
                if (it.isNotEmpty()) {
                    return@flatMap mergeNews(it, nextPage, previousPage)
                } else {
                    return@flatMap fetchAndPersistNews(nextPage, previousPage)
                }
            }
    }

    private fun mergeNews(
        previousNews: ArrayList<Article>,
        nextPage: Int,
        previousPage: Int
    ): Single<ArrayList<Article>> =
        deleteNews().andThen(newsApiRemoteDataSource.getNews(nextPage))
            .map { it.articles }
            .map { news ->
                previousNews.forEach {
                    news.add(it)
                }
                previousNews
            }
            .doOnSuccess {
                persistNews(previousNews, nextPage, previousPage)
            }
            .doOnError {
                Single.just(arrayListOf(Article))
            }

    @SuppressLint("CheckResult")
    private fun fetchAndPersistNews(nextPage: Int, previousPage: Int): Single<ArrayList<Article>> =
        newsApiRemoteDataSource.getNews(nextPage)
            .map { it.articles }
            .doOnSuccess {
                Log.i(TAG, "Dispatching ${it.size} news from API...")
                persistNews(it, nextPage, previousPage)
            }
            .doOnError {
                Single.just(arrayListOf(Article))
            }

    @SuppressLint("CheckResult")
    private fun persistNews(
        news: ArrayList<Article>,
        nextPage: Int,
        previousPage: Int
    ) {
        insertNews(*ListUtils.toArray(Article::class.java, news))
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .doOnComplete { newsApiLocalDataSource.updatePageNumber(nextPage, previousPage) }
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