package com.x0.newsapi.data.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import javax.inject.Singleton

@Singleton
class PaginationNewsRepository(context: Context) :
    SharedPreferencesRepository(
        context.getSharedPreferences(
            NEWS_PAGINATION_REPOSITORY,
            MODE_PRIVATE
        )
    ) {

    companion object {
        private const val NEWS_PAGINATION_REPOSITORY = "com.x0.newsapi.NEWS_PAGINATION"
        private const val NEWS_PAGE_ID = "NEWS_PAGE_ID"
        private const val SHOULD_LOAD_MORE_NEWS = "SHOULD_LOAD_MORE_NEWS"
        private const val NEWS_TOTAL_RESULTS = "NEWS_TOTAL_RESULTS"
    }

    fun getNewsPageNumber(): Int {
        return sharedPreferences.getInt(NEWS_PAGE_ID, 0)
    }

    fun putNewsPageNumber(pagerNumber: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(NEWS_PAGE_ID, pagerNumber).apply()
    }

    fun shouldLoadMoreNews(): Boolean {
        return sharedPreferences.getBoolean(SHOULD_LOAD_MORE_NEWS, true)
    }

    fun putShouldLoadMoreNews(shoudLoadMoreNews: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(SHOULD_LOAD_MORE_NEWS, shoudLoadMoreNews).apply()
    }

    fun getNewsTotalResults(): Int {
        val int = sharedPreferences.getInt(NEWS_TOTAL_RESULTS, 0)
        return int
    }

    fun putNewsTotalResults(newsTotalResults: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(NEWS_TOTAL_RESULTS, newsTotalResults).apply()
    }
}
