package com.x0.newsapi.di.module

import android.app.Application
import android.content.Context
import android.content.res.Resources
import com.x0.newsapi.NewsApiApplication
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class ApplicationModule(private val application: NewsApiApplication) {

    @Provides
    @Singleton
    fun provideApplication(): Application = application

    @Provides
    internal fun provideContext(): Context = application.applicationContext
}
