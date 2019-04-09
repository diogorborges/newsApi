package com.x0.newsapi.data.local

import android.content.Context
import android.content.Context.MODE_PRIVATE
import javax.inject.Singleton

@Singleton
class PaginationRepository(context: Context) :
    SharedPreferencesRepository(context.getSharedPreferences(PAGINATION_REPOSITORY, MODE_PRIVATE)) {

    companion object {
        private const val PAGINATION_REPOSITORY = "com.x0.newsapi.PAGINATION"
        private const val PAGE_ID = "PAGE_ID"
        private const val SHOULD_LOAD_MORE = "SHOULD_LOAD_MORE"
    }

    fun getPageNumber(): Int {
        return sharedPreferences.getInt(PAGE_ID, 0)
    }

    fun putPageNumber(pagerNumber: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(PAGE_ID, pagerNumber).apply()
    }

    fun shouldLoadMore(): Boolean {
        return sharedPreferences.getBoolean(SHOULD_LOAD_MORE, true)
    }

    fun putShouldLoadMore(shoudLoadMore: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(SHOULD_LOAD_MORE, shoudLoadMore).apply()
    }
}
