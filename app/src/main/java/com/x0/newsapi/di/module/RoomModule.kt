package com.x0.newsapi.di.module

import android.content.Context
import androidx.room.Room
import com.x0.newsapi.NewsApiApplication
import com.x0.newsapi.data.local.NewsApiDatabase
import com.x0.newsapi.data.local.NewsDao
import com.x0.newsapi.data.local.SourcesDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule() {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): NewsApiDatabase =
        Room.databaseBuilder(context, NewsApiDatabase::class.java, "NewsApi.db").build()

    @Provides
    @Singleton
    fun provideSourcesDao(database: NewsApiDatabase): SourcesDao = database.sourcesDao()

    @Provides
    @Singleton
    fun provideNewsDao(database: NewsApiDatabase): NewsDao = database.newsDao()
}