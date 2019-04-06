package com.x0.newsapi.presentation.news

import com.x0.newsapi.data.model.news.Article

interface NewsContract {

    interface View {
        fun showLoader(show: Boolean)
        fun showNews(newsList: ArrayList<Article>)
        fun changeFavoriteStatus(position: Int)
        fun showError(message: String?)
    }

    interface Presenter {
        fun refreshList()
        fun updateFavoriteStatus(news: Article, position: Int)
        fun setView(newsFragment: NewsFragment)
    }
}