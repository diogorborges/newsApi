package com.x0.newsapi.presentation.sourcelist

import android.util.Log
import com.x0.newsapi.R
import com.x0.newsapi.common.addTo
import com.x0.newsapi.data.model.news.Article
import com.x0.newsapi.data.usecase.ArticleUseCase
import com.x0.newsapi.presentation.ui.ListHeader
import com.x0.newsapi.presentation.ui.GenericListItem
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class ArticleListPresenter @Inject constructor(
    private val articleUseCase: ArticleUseCase
) : ArticleListContract.Presenter {

    private val loadMoreNewsObserver = PublishSubject.create<Article>()

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var view: ArticleListContract.View

    private var pageNumber: Int = 0
    private var isRefreshing: Boolean = false
    private lateinit var sourceId: String

    companion object {
        private const val TAG = "ArticleListPresenter"
    }

    override fun setView(
        articleList: ArticleListFragment,
        id: String
    ) {
        view = articleList
        sourceId = id

        setupLoadMoreNewsChangedEvent()
        getArticleList(isRefreshing = false)
    }

    private fun setupLoadMoreNewsChangedEvent(): Disposable =
        loadMoreNewsObserver
            .doOnNext { articleUseCase.saveShouldLoadArticles(true) }
            .subscribe { getArticleList(isRefreshing = false) }

    private fun getArticleList(isRefreshing: Boolean) {
        this.isRefreshing = isRefreshing

        if (!hasReachedTotalResults()) {
            val disposable = articleUseCase.getArticles(sourceId, getPageNumber())
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
        } else {
            view.showRefreshing(false)
        }
    }

    private fun hasReachedTotalResults(): Boolean =
        articleUseCase.getArticlesPageNumber() > 0 && (articleUseCase.getArticlesListSize() == articleUseCase.getArticlesTotalResult())

    private fun setLoaders(isRefreshing: Boolean, showLoader: Boolean) = when (isRefreshing) {
        true -> view.showRefreshing(showLoader)
        else -> view.showLoader(showLoader)
    }

    private fun getPageNumber(): Int =
        when (articleUseCase.shouldLoadMoreArticles()) {
            true -> {
                articleUseCase.saveShouldLoadArticles(false)
                pageNumber = articleUseCase.getArticlesPageNumber()
                ++pageNumber
            }
            else -> {
                pageNumber = articleUseCase.getArticlesPageNumber()
                pageNumber
            }
        }

    override fun refreshList() = getArticleList(isRefreshing = true)

    private fun onSuccess(articleList: ArrayList<Article>) {
        if (isRefreshing) view.clearArticleList()

        view.showArticleList(prepareListNewsList(articleList))
    }

    private fun prepareListNewsList(articleList: ArrayList<Article>): List<AbstractFlexibleItem<*>> {
        val listItems = ArrayList<AbstractFlexibleItem<*>>()

        val listHeader = ListHeader(R.string.article_header, R.layout.list_header)
        articleList.forEach {
            listItems.add(
                GenericListItem(
                    listHeader,
                    it,
                    loadMoreNewsObserver,
                    articleList.size
                )
            )
        }

        return listItems
    }

    private fun onError(throwable: Throwable) = view.showError(throwable.message)

    fun clearArticles() = articleUseCase.clearArticles()

    fun destroy() = compositeDisposable.dispose()
}