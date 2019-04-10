package com.x0.newsapi.data.repository

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.x0.newsapi.common.ListUtils
import com.x0.newsapi.common.hasNetwork
import com.x0.newsapi.data.FailureException
import com.x0.newsapi.data.NetworkException
import com.x0.newsapi.data.local.NewsApiLocalDataSource
import com.x0.newsapi.data.model.sources.Source
import com.x0.newsapi.data.remote.NewsApiRemoteDataSource
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SourcesRepository @Inject constructor(
    private val newsApiRemoteDataSource: NewsApiRemoteDataSource,
    private val newsApiLocalDataSource: NewsApiLocalDataSource,
    private val context: Context
) {

    companion object {
        private const val TAG = "SourcesRepository"
    }

    fun getSources(): Single<ArrayList<Source>> =
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
                        Log.i(TAG, "Error ${it.message}")
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

    private fun insertSources(vararg sources: Source): Completable =
        newsApiLocalDataSource.insertSources(*sources)
}