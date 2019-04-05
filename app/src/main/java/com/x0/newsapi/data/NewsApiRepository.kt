package com.x0.newsapi.data

import android.annotation.SuppressLint
import com.x0.newsapi.common.ListUtils
import com.x0.newsapi.data.local.NewsApiLocalDataSource
import com.x0.newsapi.data.model.news.NewsResponse
import com.x0.newsapi.data.model.sources.Source
import com.x0.newsapi.data.remote.NewsApiRemoteDataSource
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NewsApiRepository @Inject constructor(
    val newsApiRemoteDataSource: NewsApiRemoteDataSource,
    val newsApiLocalDataSource: NewsApiLocalDataSource
) : NewsApiDataSource {

    override fun getFavoriteSources(isFavorite: Boolean): Single<ArrayList<Source>> =
        newsApiLocalDataSource.getFavoriteSources(isFavorite)

    override fun updateFavoriteSource(newsId: String, isFavorite: Boolean): Completable =
        newsApiLocalDataSource.updateFavoriteSource(newsId, isFavorite)

    override fun getSources(): Single<ArrayList<Source>> =
        newsApiLocalDataSource.getSources()
            .flatMap {
                if (it.isNotEmpty()) {
                    return@flatMap Single.just(it)
                } else {
                    return@flatMap deleteAndFetchSources()
                }
            }

    private fun deleteAndFetchSources(): Single<ArrayList<Source>> =
        deleteSources().andThen(fetchAndPersistSources())

    private fun fetchAndPersistSources(): Single<ArrayList<Source>> =
        newsApiRemoteDataSource.getSources().doOnSuccess(this::persistSources)

    @SuppressLint("CheckResult")
    private fun persistSources(sources: ArrayList<Source>) {
        insertSources(*ListUtils.toArray(Source::class.java, sources))
            .subscribeOn(Schedulers.io())
    }

    override fun insertSources(vararg sources: Source): Completable =
        newsApiLocalDataSource.insertSources(*sources)

    override fun deleteSources(): Completable = newsApiLocalDataSource.deleteSources()

    override fun getSourcesBySubject(subject: String): Single<NewsResponse> =
        newsApiRemoteDataSource.getSourcesBySubject(subject)

    override fun getSourceById(sources: String): Single<NewsResponse> =
        newsApiRemoteDataSource.getSourceById(sources)
}
