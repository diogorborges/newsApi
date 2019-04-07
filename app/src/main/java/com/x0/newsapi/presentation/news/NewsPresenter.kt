package com.x0.newsapi.presentation.news

import android.util.Log
import com.x0.newsapi.R
import com.x0.newsapi.common.addTo
import com.x0.newsapi.data.model.news.Article
import com.x0.newsapi.data.usecase.NewsUseCase
import com.x0.newsapi.presentation.ListHeader
import com.x0.newsapi.presentation.NewsListItem
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class NewsPresenter @Inject constructor(
    private val newsUseCase: NewsUseCase
) : NewsContract.Presenter {

    private val openNewsDetailsObserver = PublishSubject.create<Article>()
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var view: NewsContract.View
    private var pageNumber: Int = 0

    companion object {
        private const val TAG = "NewsPresenter"
    }

    override fun setView(newsFragment: NewsFragment) {
        view = newsFragment
        setupOpenNewsDetailsChangedEvent()
        getNews()
    }

    private fun setupOpenNewsDetailsChangedEvent(): Disposable =
        openNewsDetailsObserver
            .subscribe(
                { view.onNewsDetailsClicked(it) },
                { Log.e(TAG, "Error: $it") })

    private fun getNews() {
        view.showLoader(true)

        val nextPageNumber = getNextPage()

        val disposable = newsUseCase.getNews(nextPageNumber)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onSuccess, this::onError)
        disposable.addTo(compositeDisposable)
    }

    private fun getNextPage(): Int =
        if (newsUseCase.isFirstLoad()) {
            newsUseCase.saveIsFirstLoad(false)
            ++pageNumber
        } else {
            pageNumber = newsUseCase.getPageNumber()
            ++pageNumber
        }

    override fun refreshList() {
        getNews()
    }

    private fun onSuccess(newsList: ArrayList<Article>) {
        view.showNews(prepareListNewsList(newsList))
    }

    private fun prepareListNewsList(newsList: ArrayList<Article>): List<AbstractFlexibleItem<*>> {
        val listItems = java.util.ArrayList<AbstractFlexibleItem<*>>()

        newsList.forEach {
            listItems.add(
                NewsListItem(
                    ListHeader(
                        R.layout.list_header,
                        R.string.news_header
                    ), it, openNewsDetailsObserver
                )
            )
        }

        return listItems
    }

    private fun onError(throwable: Throwable) {
        view.showError(throwable.message)
    }
}