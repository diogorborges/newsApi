package com.x0.newsapi.presentation.sourcelist

import android.util.Log
import com.x0.newsapi.R
import com.x0.newsapi.data.model.news.Article
import com.x0.newsapi.data.usecase.ArticleUseCase
import com.x0.newsapi.presentation.ListHeader
import com.x0.newsapi.presentation.NewsListItem
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

    private val openArticleDetailsObserver = PublishSubject.create<Article>()
    private val loadMoreNewsObserver = PublishSubject.create<Article>()

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var view: ArticleListContract.View

    private var pageNumber: Int = 0
    private var isRefreshing: Boolean = false

    companion object {
        private const val TAG = "ArticleListPresenter"
    }

    override fun setView(
        articleList: ArticleListFragment,
        id: String
    ) {
        view = articleList
        setupOpenArticleDetailsChangedEvent()
        setupLoadMoreNewsChangedEvent()
        getArticleList(isRefreshing = false, id = id)
    }

    private fun setupOpenArticleDetailsChangedEvent(): Disposable =
        openArticleDetailsObserver
            .subscribe(
                { view.onArticleClicked(it) },
                { Log.e(TAG, "Error: $it") })

    private fun setupLoadMoreNewsChangedEvent(): Disposable =
        loadMoreNewsObserver
            .doOnNext { articleUseCase.saveShouldLoadMore(true) }
            .subscribe(
                { getArticleList(isRefreshing = false, id = it) },
                { Log.e(TAG, "Error: $it") })

    private fun getArticleList(isRefreshing: Boolean, id: String) {
        this.isRefreshing = isRefreshing

        val disposable = articleUseCase.getNews(id, getPageNumber(), isRefreshing)
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
        when (articleUseCase.shouldLoadMore()) {
            true -> {
                articleUseCase.saveShouldLoadMore(false)
                pageNumber = articleUseCase.getPageNumber()
                ++pageNumber
            }
            else -> {
                pageNumber = articleUseCase.getPageNumber()
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
                NewsListItem(
                    listHeader,
                    it,
                    openArticleDetailsObserver,
                    loadMoreNewsObserver,
                    articleList.size
                )
            )
        }

        return listItems
    }

    private fun onError(throwable: Throwable) {
    }
}