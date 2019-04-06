package com.x0.newsapi.presentation.news

import com.x0.newsapi.common.addTo
import com.x0.newsapi.data.model.news.Article
import com.x0.newsapi.data.usecase.NewsUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class NewsPresenter @Inject constructor(
    private val newsUseCase: NewsUseCase
) : NewsContract.Presenter {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var view: NewsContract.View
    private var pageNumber: Int = 0

    override fun setView(newsFragment: NewsFragment) {
        view = newsFragment
        getNews()
    }

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
        view.showNews(newsList)
    }

    private fun onError(throwable: Throwable) {
        view.showError(throwable.message)
    }

    override fun updateFavoriteStatus(news: Article, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}