package com.x0.newsapi.di.module

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.x0.newsapi.data.local.PaginationArticlesRepository
import com.x0.newsapi.data.local.PaginationNewsRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule {

    private val NEWS_API_SHARED_PREF = "NEWS_API_SHARED_PREF"

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Context): SharedPreferences =
        context.getSharedPreferences(NEWS_API_SHARED_PREF, MODE_PRIVATE)

    @Provides
    fun providesPaginationNewsRepository(context: Context): PaginationNewsRepository =
        PaginationNewsRepository(context)

    @Provides
    fun providesPaginationArticlesRepository(context: Context): PaginationArticlesRepository =
        PaginationArticlesRepository(context)
}