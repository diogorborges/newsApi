package com.x0.newsapi.di.component

import com.x0.newsapi.di.module.ApplicationModule
import com.x0.newsapi.NewsApiApplication
import dagger.Component

@Component(modules = arrayOf(ApplicationModule::class))
interface ApplicationComponent {

    fun inject(application: NewsApiApplication)

}
