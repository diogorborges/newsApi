package com.x0.newsapi.presentation.sources

import com.x0.newsapi.data.model.sources.Source

interface SourcesContract {

    interface View {
        fun showLoader(show: Boolean)
        fun showSources(sourceList: ArrayList<Source>)
        fun changeFavoriteStatus(position: Int)
        fun showError(message: String?)
    }

    interface Presenter {
        fun refreshList()
        fun updateFavoriteStatus(source: Source, position: Int)
        fun setView(sourcesFragment: SourcesFragment)
    }
}