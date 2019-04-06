package com.x0.newsapi.di.module

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.x0.newsapi.data.local.PaginationRepository
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
    fun providesPageRepository(context: Context): PaginationRepository = PaginationRepository(context)
}