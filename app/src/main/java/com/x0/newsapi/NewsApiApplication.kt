package com.x0.newsapi

import android.app.Application
import com.x0.newsapi.di.component.ApplicationComponent
import com.x0.newsapi.di.module.ApplicationModule

class NewsApiApplication : Application() {

    lateinit var component: ApplicationComponent

    companion object {
        lateinit var instance: NewsApiApplication private set
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        setupDagger()
    }

    private fun setupDagger() {
        component = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()
        component.inject(this)
    }

    fun getApplicationComponent(): ApplicationComponent = component
}
