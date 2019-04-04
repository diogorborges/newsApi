package com.x0.newsapi

import android.app.Application
import com.x0.newsapi.di.component.ApplicationComponent
import com.x0.newsapi.di.component.DaggerApplicationComponent
import com.x0.newsapi.di.module.ApplicationModule

class NewsApiApplication : Application() {

    lateinit var applicationComponent: ApplicationComponent

    companion object {
        lateinit var instance: NewsApiApplication
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        setupDagger()
    }

    private fun setupDagger() {
        applicationComponent = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this)).build()

        applicationComponent.inject(this)
    }
}
