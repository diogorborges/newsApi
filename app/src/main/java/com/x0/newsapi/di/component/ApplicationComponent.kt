package com.x0.newsapi.di.component

import com.x0.newsapi.NewsApiApplication
import com.x0.newsapi.data.remote.ApiService
import com.x0.newsapi.di.module.ApplicationModule
import com.x0.newsapi.di.module.RestModule
import com.x0.newsapi.presentation.MainActivity
import com.x0.newsapi.presentation.SourcesFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApplicationModule::class, RestModule::class])
interface ApplicationComponent {

    fun apiService(): ApiService

    fun inject(application: NewsApiApplication)

    fun inject(activity: MainActivity)

    fun inject(fragment: SourcesFragment)
}