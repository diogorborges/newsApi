package com.x0.newsapi.common

import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

fun Disposable?.addTo(compositeDisposable: CompositeDisposable) {
    this?.also { compositeDisposable.add(it) }
}
