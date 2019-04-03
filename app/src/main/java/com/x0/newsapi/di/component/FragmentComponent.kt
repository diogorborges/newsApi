package com.x0.newsapi.di.component

import com.x0.newsapi.FavoritesFragment
import com.x0.newsapi.SearchFragment
import com.x0.newsapi.SourcesFragment
import com.x0.newsapi.di.module.FragmentModule
import dagger.Component

@Component(modules = arrayOf(FragmentModule::class))
interface FragmentComponent {

    fun inject(fragment: SourcesFragment)

    fun inject(fragment: SearchFragment)

    fun inject(fragment: FavoritesFragment)
}