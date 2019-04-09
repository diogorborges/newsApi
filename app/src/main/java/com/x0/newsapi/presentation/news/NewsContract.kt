package com.x0.newsapi.presentation.news

import com.x0.newsapi.data.model.news.Article
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem

interface NewsContract {

    interface View {
        fun showLoader(show: Boolean)
        fun showNews(newsList: List<AbstractFlexibleItem<*>>)
        fun showError(message: String?)
        fun onNewsDetailsClicked(article: Article)
        fun clearNewsList()
        fun showRefreshing(show: Boolean)
    }

    interface Presenter {
        fun refreshList()
        fun setView(newsFragment: NewsFragment)
    }
}