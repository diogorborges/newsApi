package com.x0.newsapi.presentation.sources

import com.x0.newsapi.data.model.sources.Source
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem

interface SourcesContract {

    interface View {
        fun showLoader(show: Boolean)
        fun showSources(sourcesList: List<AbstractFlexibleItem<*>>)
        fun showError(message: String?)
        fun onSourceClicked(source: Source)
        fun clearSourcesList()
        fun showRefreshing(show: Boolean)
    }

    interface Presenter {
        fun refreshList()
        fun setView(sourcesFragment: SourcesFragment)
    }
}