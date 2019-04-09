package com.x0.newsapi.presentation.sourcelist

import com.x0.newsapi.data.model.news.Article
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem

interface ArticleListContract {

    interface View {
        fun showLoader(show: Boolean)
        fun showArticleList(articleList: List<AbstractFlexibleItem<*>>)
        fun showError(message: String?)
        fun onArticleClicked(article: Article)
        fun clearArticleList()
        fun showRefreshing(show: Boolean)
    }

    interface Presenter {
        fun refreshList()
        fun setView(
            articleList: ArticleListFragment,
            id: String
        )
    }
}
