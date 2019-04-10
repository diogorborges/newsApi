package com.x0.newsapi.data.local

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Singleton

@Singleton
class PaginationArticlesRepository(context: Context) :
    SharedPreferencesRepository(
        context.getSharedPreferences(
            ARTICLES_PAGINATION_REPOSITORY,
            Context.MODE_PRIVATE
        )
    ) {

    companion object {
        private const val ARTICLES_PAGINATION_REPOSITORY = "com.x0.newsapi.ARTICLES_PAGINATION"
        private const val ARTICLES_PAGE_ID = "ARTICLES_PAGE_ID"
        private const val SHOULD_LOAD_MORE_ARTICLES = "SHOULD_LOAD_MORE_ARTICLES"
        private const val ARTICLES_TOTAL_RESULTS = "ARTICLES_TOTAL_RESULTS"
        private const val ARTICLES_LIST_SIZE = "ARTICLES_LIST_SIZE"
    }

    fun getArticlesPageNumber(): Int {
        val int = sharedPreferences.getInt(ARTICLES_PAGE_ID, 0)
        return int
    }

    fun putArticlesPageNumber(pagerNumber: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(ARTICLES_PAGE_ID, pagerNumber).apply()
    }

    fun shouldLoadMoreArticles(): Boolean =
        sharedPreferences.getBoolean(SHOULD_LOAD_MORE_ARTICLES, true)

    fun putShouldLoadMoreArticles(shoudLoadMoreNews: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(SHOULD_LOAD_MORE_ARTICLES, shoudLoadMoreNews).apply()
    }

    fun getArticlesTotalResults(): Int {
        val int = sharedPreferences.getInt(ARTICLES_TOTAL_RESULTS, 0)
        return int
    }

    fun putArticlesTotalResults(newsTotalResults: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(ARTICLES_TOTAL_RESULTS, newsTotalResults).apply()
    }

    fun getArticlesListSize(): Int {
        val int = sharedPreferences.getInt(ARTICLES_LIST_SIZE, 0)
        return int
    }

    fun putArticleListSize(articleListSize: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(ARTICLES_LIST_SIZE, articleListSize).apply()
    }

    fun clearArticles()  {
        val editor = sharedPreferences.edit()
        editor.remove(ARTICLES_LIST_SIZE);
        editor.remove(ARTICLES_TOTAL_RESULTS);
        editor.remove(SHOULD_LOAD_MORE_ARTICLES);
        editor.remove(ARTICLES_PAGE_ID);
        editor.apply();
    }

}
