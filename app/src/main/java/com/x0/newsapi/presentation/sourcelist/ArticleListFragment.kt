package com.x0.newsapi.presentation.sourcelist

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.x0.newsapi.NewsApiApplication
import com.x0.newsapi.R
import com.x0.newsapi.common.gone
import com.x0.newsapi.common.inflate
import com.x0.newsapi.common.visible
import com.x0.newsapi.data.model.news.Article
import com.x0.newsapi.data.model.sources.Source
import com.x0.newsapi.presentation.sources.SourcesFragment
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import kotlinx.android.synthetic.main.fragment_generic_news.genericList
import kotlinx.android.synthetic.main.fragment_generic_news.progressBarLayout
import kotlinx.android.synthetic.main.fragment_generic_news.swipeRefresh
import javax.inject.Inject

class ArticleListFragment : Fragment(), ArticleListContract.View, SwipeRefreshLayout.OnRefreshListener {

    @Inject
    lateinit var presenter: ArticleListPresenter

    private lateinit var adapter: FlexibleAdapter<AbstractFlexibleItem<*>>

    companion object {
        private const val TAG = "SourcesFragment"
        private const val KEY = "Article"
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
        container?.inflate(R.layout.fragment_generic_news)

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val source = arguments?.getParcelable<Source>(SourcesFragment.KEY)

        source?.id?.let {
            presenter.setView(this, it)
        }

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

    override fun onStop() {
        super.onStop()

        presenter.clearArticles()
    }

    override fun showRefreshing(show: Boolean) = with(swipeRefresh) {
        isRefreshing = show
    }

    override fun onArticleClicked(article: Article) {
        Log.i(TAG, "Article clicked: $article")
//        val bundle = Bundle()
//        bundle.putParcelable(KEY, article)
//
//        val fragment = ArticleDetailsFragment()
//        fragment.arguments = bundle
//
//        (context as MainActivity)
//            .supportFragmentManager
//            .beginTransaction()
//            .add(R.id.main_container, fragment, MainActivity.FRAGMENT_KEY)
//            .addToBackStack(null)
//            .commit()
    }

    override fun showLoader(show: Boolean) = with(progressBarLayout) {
        when (show) {
            true -> visible()
            else -> gone()
        }
    }

    override fun clearArticleList() = adapter.clear()

    override fun showArticleList(articleList: List<AbstractFlexibleItem<*>>) =
        adapter.updateDataSet(articleList)

    override fun showError(message: String?) {
        message?.let {
            Log.e(TAG, "Error: $it")
        }
    }
}
