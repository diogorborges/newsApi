package com.x0.newsapi.presentation.news

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.x0.newsapi.NewsApiApplication
import com.x0.newsapi.common.gone
import com.x0.newsapi.common.inflate
import com.x0.newsapi.common.visible
import com.x0.newsapi.data.model.news.Article
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import kotlinx.android.synthetic.main.fragment_generic_news.genericList
import kotlinx.android.synthetic.main.fragment_generic_news.progressBarLayout
import kotlinx.android.synthetic.main.fragment_generic_news.swipeRefresh
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
        container?.inflate(com.x0.newsapi.R.layout.fragment_generic_news)

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setView(this)
        setupUI()
    }

    private fun setupUI() {
        swipeRefresh.setOnRefreshListener(this)

        adapter = FlexibleAdapter(ArrayList<AbstractFlexibleItem<*>>())
        adapter.isAnimateChangesWithDiffUtil = true

        genericList.adapter = adapter
        genericList.layoutManager = LinearLayoutManager(context)
        genericList.isNestedScrollingEnabled = true
    }

    override fun onRefresh() = presenter.refreshList()

    override fun showRefreshing(show: Boolean) = with(swipeRefresh) {
        isRefreshing = show
    }

    override fun showLoader(show: Boolean) = with(progressBarLayout) {
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
