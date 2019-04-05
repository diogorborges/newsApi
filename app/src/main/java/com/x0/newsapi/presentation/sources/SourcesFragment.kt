package com.x0.newsapi.presentation.sources

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.x0.newsapi.NewsApiApplication
import com.x0.newsapi.R
import com.x0.newsapi.common.inflate
import com.x0.newsapi.data.remote.NewApiService
import com.x0.newsapi.data.model.sources.SourcesResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_sources.messageTextView
import javax.inject.Inject

class SourcesFragment : Fragment() {

    @Inject
    lateinit var newApi: NewApiService

    companion object {
        const val TITLE = "Source"
        private const val TAG = "SourcesFragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NewsApiApplication.instance.applicationComponent.inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? =
        container?.inflate(R.layout.fragment_sources)

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        getSources()
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .subscribe(::onSuccess, ::onError)
    }

    private fun getSources(): Single<SourcesResponse> =
        newApi.getSources()

    private fun onSuccess(sourcesResponse: SourcesResponse) {
        messageTextView.text = "Sources ${sourcesResponse.status}"
    }

    private fun onError(throwable: Throwable) {
        throwable.message?.let { messageTextView.text = "Error $it" }
    }

}
