package com.x0.newsapi

import android.app.Application
import com.x0.newsapi.di.component.ApplicationComponent
import com.x0.newsapi.di.component.DaggerApplicationComponent
import com.x0.newsapi.di.module.ApplicationModule
import com.x0.newsapi.di.module.RestModule
import com.x0.newsapi.di.module.RoomModule

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
            .restModule(RestModule(this))
            .applicationModule(ApplicationModule())
            .roomModule(RoomModule())
            .build()

        applicationComponent.inject(this)
    }
}
