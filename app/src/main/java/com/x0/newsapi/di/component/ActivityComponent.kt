package com.x0.newsapi.di.component

import com.x0.newsapi.MainActivity
import com.x0.newsapi.di.module.ActivityModule
import dagger.Component

@Component(modules = arrayOf(ActivityModule::class))
interface ActivityComponent {

    fun inject(activity: MainActivity)

}