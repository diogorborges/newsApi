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
import com.x0.newsapi.data.model.sources.Source
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import kotlinx.android.synthetic.main.fragment_news.messageText
import javax.inject.Inject

class SourcesFragment : Fragment(), SourcesContract.View {

    @Inject
    lateinit var presenter: SourcesPresenter

    private lateinit var adapter: FlexibleAdapter<AbstractFlexibleItem<*>>

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
        presenter.setView(this)
        setupUI()
    }

    private fun setupUI() {
//        adapter = FlexibleAdapter(java.util.ArrayList<AbstractFlexibleItem<*>>())
//        adapter.setDisplayHeadersAtStartUp(true)
//        adapter.isAnimateChangesWithDiffUtil = true
//
//        taskList.adapter = adapter
//        taskList.layoutManager = LinearLayoutManager(context)
//        taskList.isNestedScrollingEnabled = false
    }

    override fun showLoader(show: Boolean) {
    }

    override fun showSources(sourceList: ArrayList<Source>) {
        messageText.text = "Sources ${sourceList}"
    }

    override fun changeFavoriteStatus(position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showError(message: String?) {
        message?.let { messageText.text = "Error $it" }
    }

}
