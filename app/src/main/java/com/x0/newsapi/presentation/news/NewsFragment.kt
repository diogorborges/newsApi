package com.x0.newsapi.presentation.news

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.x0.newsapi.NewsApiApplication
import com.x0.newsapi.R
import com.x0.newsapi.common.gone
import com.x0.newsapi.common.inflate
import com.x0.newsapi.common.visible
import com.x0.newsapi.data.model.news.Article
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import kotlinx.android.synthetic.main.fragment_news.swipeRefresh
import kotlinx.android.synthetic.main.fragment_sources.newsList
import kotlinx.android.synthetic.main.progress_bar.loadingProgressBar
import javax.inject.Inject

class NewsFragment : Fragment(), NewsContract.View, OnRefreshListener {

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
        swipeRefresh.setOnRefreshListener(this)

        adapter = FlexibleAdapter(ArrayList<AbstractFlexibleItem<*>>())
        adapter.setDisplayHeadersAtStartUp(true)
        adapter.isAnimateChangesWithDiffUtil = true

        newsList.adapter = adapter
        newsList.layoutManager = LinearLayoutManager(context)
        newsList.isNestedScrollingEnabled = true
    }

    override fun onRefresh() = presenter.refreshList()

    override fun showRefreshing(show: Boolean) = with(swipeRefresh) {
        isRefreshing = show
    }

    override fun showLoader(show: Boolean) = with(loadingProgressBar) {
        when (show) {
            true -> visible()
            else -> gone()
        }
    }

    override fun clearNewsList() = adapter.clear()

    override fun showNews(newsList: List<AbstractFlexibleItem<*>>) = adapter.updateDataSet(newsList)

    override fun showError(message: String?) {
        message?.let {
            Log.e(TAG, "Error: $it")
        }
    }

    override fun onNewsDetailsClicked(article: Article) = Unit
}
