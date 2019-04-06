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
        private const val IS_FIRST_LOAD = "IS_FIRST_LOAD"
    }

//    var pageNumber: Int
//        get() = sharedPreferences.getInt(PAGE_ID, 0)
//        set(value) = with(sharedPreferences.edit()) {
//            putInt(PAGE_ID, pageNumber)
//            apply()
//        }

//    var isFirstLoad: Boolean
//        get() = sharedPreferences.getBoolean(IS_FIRST_LOAD, true)
//        set(value) = with(sharedPreferences.edit()) {
//            putBoolean(IS_FIRST_LOAD, isFirstLoad)
//            apply()
//        }

    fun getPageNumber(): Int {
        val int = sharedPreferences.getInt(PAGE_ID, 0)
        return int
    }

    fun putPageNumber(pagerNumber: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(PAGE_ID, pagerNumber).apply()
    }

    fun isFirstLoad(): Boolean {
        val boolean = sharedPreferences.getBoolean(IS_FIRST_LOAD, true)
        return boolean
    }

    fun putFirstLoad(firstLoad: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(IS_FIRST_LOAD, firstLoad).apply()
    }
}
