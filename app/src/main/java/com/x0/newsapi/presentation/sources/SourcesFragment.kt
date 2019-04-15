package com.x0.newsapi.presentation.sources

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
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
import com.x0.newsapi.data.model.sources.Source
import com.x0.newsapi.presentation.MainActivity
import com.x0.newsapi.presentation.sourcelist.ArticleListFragment
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import kotlinx.android.synthetic.main.fragment_sources.errorText
import kotlinx.android.synthetic.main.fragment_sources.progressBarLayout
import kotlinx.android.synthetic.main.fragment_sources.sourcesList
import javax.inject.Inject

class SourcesFragment : Fragment(), SourcesContract.View {

    @Inject
    lateinit var presenter: SourcesPresenter

    private lateinit var adapter: FlexibleAdapter<AbstractFlexibleItem<*>>

    companion object {
        const val TITLE = "Sources"
        private const val TAG = "SourcesFragment"
        const val KEY = "Source"
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

        setupUI()
        presenter.setView(this)
    }

    private fun setupUI() {
        (activity as MainActivity).showBackButton(false)

        adapter = FlexibleAdapter(ArrayList<AbstractFlexibleItem<*>>())
        adapter.isAnimateChangesWithDiffUtil = true

        sourcesList.adapter = adapter
        sourcesList.layoutManager = LinearLayoutManager(context)
        sourcesList.isNestedScrollingEnabled = true
    }

    private fun showErrorMessage() {
        sourcesList.gone()
        errorText.visible()
    }

    override fun onSourceClicked(source: Source) {
        val bundle = Bundle()
        bundle.putParcelable(KEY, source)

        val fragment = ArticleListFragment()
        fragment.arguments = bundle

        (context as MainActivity)
            .supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .add(R.id.main_container, fragment, MainActivity.FRAGMENT_KEY)
            .addToBackStack(null)
            .commit()
    }

    override fun showLoader(show: Boolean) = with(progressBarLayout) {
        when (show) {
            true -> visible()
            else -> gone()
        }
    }

    override fun showSources(sourcesList: List<AbstractFlexibleItem<*>>) =
        adapter.updateDataSet(sourcesList)

    override fun showError(message: String?) {
        message?.let {
            Log.e(TAG, "Error: $it")
            showErrorMessage()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        presenter.destroy()
    }
}