package com.x0.newsapi.presentation.news

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.x0.newsapi.NewsApiApplication
import com.x0.newsapi.R
import com.x0.newsapi.common.gone
import com.x0.newsapi.common.inflate
import com.x0.newsapi.common.visible
import com.x0.newsapi.data.model.news.Article
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import kotlinx.android.synthetic.main.fragment_sources.newsList
import kotlinx.android.synthetic.main.progress_bar.loadingProgressBar
import javax.inject.Inject

class NewsFragment : Fragment(), NewsContract.View {

    @Inject
    lateinit var presenter: NewsPresenter

    private lateinit var adapter: FlexibleAdapter<AbstractFlexibleItem<*>>

    companion object {
        const val TITLE = "News"
        private const val TAG = "NewsFragment"
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
        container?.inflate(R.layout.fragment_news)

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setView(this)
        setupUI()
    }

    private fun setupUI() {
        adapter = FlexibleAdapter(ArrayList<AbstractFlexibleItem<*>>())
        adapter.setDisplayHeadersAtStartUp(true)
        adapter.isAnimateChangesWithDiffUtil = true

        newsList.adapter = adapter
        newsList.layoutManager = LinearLayoutManager(context)
        newsList.isNestedScrollingEnabled = true
    }

    override fun showLoader(show: Boolean) = when (show) {
        true -> loadingProgressBar.visible()
        else -> loadingProgressBar.gone()
    }

    override fun showNews(newsList: List<AbstractFlexibleItem<*>>) {
        adapter.updateDataSet(newsList)
    }

    override fun showError(message: String?) {
    }

    override fun onNewsDetailsClicked(article: Article) {
    }

}
