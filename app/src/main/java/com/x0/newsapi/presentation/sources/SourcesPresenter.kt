package com.x0.newsapi.presentation.sources

import android.util.Log
import com.x0.newsapi.R
import com.x0.newsapi.common.addTo
import com.x0.newsapi.data.model.sources.Source
import com.x0.newsapi.data.usecase.SourcesUseCase
import com.x0.newsapi.presentation.ui.ListHeader
import com.x0.newsapi.presentation.ui.SourcesListItem
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

class SourcesPresenter @Inject constructor(
    private val sourceUseCase: SourcesUseCase
) : SourcesContract.Presenter {

    private val openSourceObserver = PublishSubject.create<Source>()

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var view: SourcesContract.View

    companion object {
        private const val TAG = "SourcesPresenter"
    }

    override fun setView(sourcesFragment: SourcesFragment) {
        view = sourcesFragment
        setupOpenSourceChangedEvent()
        getSources()
    }

    private fun setupOpenSourceChangedEvent(): Disposable =
        openSourceObserver
            .subscribe(
                { view.onSourceClicked(it) },
                { Log.e(TAG, "Error: $it") })

    private fun getSources() {
        val disposable = sourceUseCase.getSources()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe {
                view.showLoader(true)
            }
            .doAfterTerminate {
                view.showLoader(false)
            }
            .subscribe(this::onSuccess, this::onError)
        disposable.addTo(compositeDisposable)
    }

    private fun onSuccess(sourceList: ArrayList<Source>) = view.showSources(prepareListNewsList(sourceList))

    private fun prepareListNewsList(sourceList: ArrayList<Source>): List<AbstractFlexibleItem<*>> {
        val listItems = ArrayList<AbstractFlexibleItem<*>>()

        val listHeader = ListHeader(R.string.sources_header, R.layout.list_header)

        sourceList.forEach { listItems.add(SourcesListItem(listHeader, it, openSourceObserver)) }

        return listItems
    }

    private fun onError(throwable: Throwable) = view.showError(throwable.message)

    fun destroy() = compositeDisposable.dispose()
}