package com.x0.newsapi.presentation.news

import android.util.Log
import com.x0.newsapi.R
import com.x0.newsapi.common.addTo
import com.x0.newsapi.data.model.news.Article
import com.x0.newsapi.data.usecase.NewsUseCase
import com.x0.newsapi.presentation.ui.GenericListItem
import com.x0.newsapi.presentation.ui.ListHeader
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.internal.disposables.DisposableHelper.dispose
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class NewsPresenter @Inject constructor(
    private val newsUseCase: NewsUseCase
) : NewsContract.Presenter {

    private val loadMoreNewsObserver = PublishSubject.create<Article>()

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var view: NewsContract.View

    private var pageNumber: Int = 0
    private var isRefreshing: Boolean = false

    companion object {
        private const val TAG = "NewsPresenter"
    }

    override fun setView(newsFragment: NewsFragment) {
        view = newsFragment
        setupLoadMoreNewsChangedEvent()
        getNews(isRefreshing = false)
    }

    private fun setupLoadMoreNewsChangedEvent(): Disposable =
        loadMoreNewsObserver
            .doOnNext { newsUseCase.saveShouldLoadMoreNews(true) }
            .subscribe(
                { getNews(isRefreshing = false) },
                { Log.e(TAG, "Error: $it") })

    private fun getNews(isRefreshing: Boolean) {
        this.isRefreshing = isRefreshing

        val disposable = newsUseCase.getNews(getPageNumber(), isRefreshing)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                setLoaders(isRefreshing, showLoader = true)
            }
            .doAfterTerminate {
                setLoaders(isRefreshing, showLoader = false)
            }
            .subscribe(this::onSuccess, this::onError)
        disposable.addTo(compositeDisposable)
    }

    private fun setLoaders(isRefreshing: Boolean, showLoader: Boolean) = when (isRefreshing) {
        true -> view.showRefreshing(showLoader)
        else -> view.showLoader(showLoader)
    }

    private fun getPageNumber(): Int =
        when (newsUseCase.shouldLoadMoreNews()) {
            true -> {
                newsUseCase.saveShouldLoadMoreNews(false)
                pageNumber = newsUseCase.getNewsPageNumber()
                ++pageNumber
            }
            else -> {
                pageNumber = newsUseCase.getNewsPageNumber()
                pageNumber
            }
        }

    override fun refreshList() = getNews(isRefreshing = true)

    private fun onSuccess(newsList: ArrayList<Article>) {
        if (isRefreshing) view.clearNewsList()

        view.showNews(prepareListNewsList(newsList))
    }

    private fun prepareListNewsList(newsList: ArrayList<Article>): List<AbstractFlexibleItem<*>> {
        val listItems = ArrayList<AbstractFlexibleItem<*>>()

        val listHeader =
            ListHeader(R.string.news_header, R.layout.list_header)
        newsList.forEach {
            listItems.add(
                GenericListItem(
                    listHeader,
                    it,
                    loadMoreNewsObserver,
                    newsList.size
                )
            )
        }

        return listItems
    }

    private fun onError(throwable: Throwable) {
        view.showError(throwable.message)
    }

    fun destroy() = compositeDisposable.dispose()
}