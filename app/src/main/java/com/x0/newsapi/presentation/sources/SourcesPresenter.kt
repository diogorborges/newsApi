package com.x0.newsapi.presentation.sources

import com.x0.newsapi.common.addTo
import com.x0.newsapi.data.model.sources.Source
import com.x0.newsapi.data.usecase.SourcesUseCase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class SourcesPresenter @Inject constructor(
    private val sourceUseCase: SourcesUseCase
) : SourcesContract.Presenter {

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private lateinit var view: SourcesContract.View

    override fun setView(sourcesFragment: SourcesFragment) {
        view = sourcesFragment
        getSources()
    }

    private fun getSources() {
        view.showLoader(true)
        val disposable = sourceUseCase.getSources()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(this::onSuccess, this::onError)
        disposable.addTo(compositeDisposable)
    }

    override fun refreshList() {
        getSources()
    }

    private fun onSuccess(sourceList: ArrayList<Source>) {
        view.showSources(sourceList)
    }

    private fun onError(throwable: Throwable) {
        view.showError(throwable.message)
    }

    override fun updateFavoriteStatus(source: Source, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}