package com.x0.newsapi.presentation.news

import eu.davidea.flexibleadapter.items.AbstractFlexibleItem

interface NewsContract {

    interface View {
        fun showLoader(show: Boolean): Unit?
        fun showNews(newsList: List<AbstractFlexibleItem<*>>)
        fun showError(message: String?)
        fun clearNewsList()
        fun showRefreshing(show: Boolean): Unit?
    }

    interface Presenter {
        fun refreshList()
        fun setView(newsFragment: NewsFragment)
    }
}