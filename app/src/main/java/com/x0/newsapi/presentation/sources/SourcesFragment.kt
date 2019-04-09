package com.x0.newsapi.presentation.sources

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.x0.newsapi.NewsApiApplication
import com.x0.newsapi.R
import com.x0.newsapi.common.gone
import com.x0.newsapi.common.inflate
import com.x0.newsapi.common.visible
import com.x0.newsapi.data.model.sources.Source
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import kotlinx.android.synthetic.main.fragment_sources.progressBarLayout
import kotlinx.android.synthetic.main.fragment_sources.sourcesList
import kotlinx.android.synthetic.main.fragment_sources.swipeRefresh
import javax.inject.Inject

class SourcesFragment : Fragment(), SourcesContract.View, SwipeRefreshLayout.OnRefreshListener {


    @Inject
    lateinit var presenter: SourcesPresenter

    private lateinit var adapter: FlexibleAdapter<AbstractFlexibleItem<*>>

    companion object {
        const val TITLE = "Sources"
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
        swipeRefresh.setOnRefreshListener(this)

        adapter = FlexibleAdapter(ArrayList<AbstractFlexibleItem<*>>())
        adapter.isAnimateChangesWithDiffUtil = true

        sourcesList.adapter = adapter
        sourcesList.layoutManager = GridLayoutManager(context, 2)
        sourcesList.isNestedScrollingEnabled = true
    }

    override fun onRefresh() = presenter.refreshList()

    override fun showRefreshing(show: Boolean) = with(swipeRefresh) {
        isRefreshing = show
    }

    override fun onSourceClicked(source: Source) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showLoader(show: Boolean) = with(progressBarLayout) {
        when (show) {
            true -> visible()
            else -> gone()
        }
    }

    override fun clearSourcesList() = adapter.clear()

    override fun showSources(sourcesList: List<AbstractFlexibleItem<*>>) =
        adapter.updateDataSet(sourcesList)

    override fun showError(message: String?) {
        message?.let {
            Log.e(TAG, "Error: $it")
        }
    }
}