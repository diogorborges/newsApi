package com.x0.newsapi.data

import android.annotation.SuppressLint
import android.util.Log
import com.x0.newsapi.common.ListUtils
import com.x0.newsapi.data.local.NewsApiLocalDataSource
import com.x0.newsapi.data.model.news.NewsResponse
import com.x0.newsapi.data.model.sources.Source
import com.x0.newsapi.data.remote.NewsApiRemoteDataSource
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
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

    override fun getSourcesBySubject(subject: String): Single<NewsResponse> =
        newsApiRemoteDataSource.getSourcesBySubject(subject)

    override fun getSourceById(sources: String): Single<NewsResponse> =
        newsApiRemoteDataSource.getSourceById(sources)
}