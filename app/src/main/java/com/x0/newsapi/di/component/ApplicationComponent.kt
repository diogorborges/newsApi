package com.x0.newsapi.di.component

import com.x0.newsapi.NewsApiApplication
import com.x0.newsapi.data.remote.NewApiService
import com.x0.newsapi.di.module.ApplicationModule
import com.x0.newsapi.di.module.RestModule
import com.x0.newsapi.di.module.RoomModule
import com.x0.newsapi.presentation.MainActivity
import com.x0.newsapi.presentation.news.NewsFragment
import com.x0.newsapi.presentation.sources.SourcesFragment
import com.x0.newsapi.presentation.sourcelist.ArticleListFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [RestModule::class, RoomModule::class, ApplicationModule::class])
interface ApplicationComponent {

    fun apiService(): NewApiService

    fun inject(application: NewsApiApplication)

    fun inject(activity: MainActivity)

    fun inject(fragment: SourcesFragment)

    fun inject(fragment: NewsFragment)

    fun inject(fragment: ArticleListFragment)

}