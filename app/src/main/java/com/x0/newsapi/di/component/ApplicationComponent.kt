package com.x0.newsapi.di.component

import com.x0.newsapi.NewsApiApplication
import com.x0.newsapi.di.module.ApplicationModule
import com.x0.newsapi.presentation.FavoritesFragment
import com.x0.newsapi.presentation.MainActivity
import com.x0.newsapi.presentation.SearchFragment
import com.x0.newsapi.presentation.SourcesFragment
import dagger.Component

@Component(modules = [ApplicationModule::class])
interface ApplicationComponent {

    fun inject(application: NewsApiApplication)

    fun inject(activity: MainActivity)

    fun inject(fragment: SearchFragment)

    fun inject(fragment: SourcesFragment)

    fun inject(fragment: FavoritesFragment)
}
