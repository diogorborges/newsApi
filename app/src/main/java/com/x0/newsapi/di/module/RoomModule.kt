package com.x0.newsapi.di.module

import android.content.Context
import androidx.room.Room
import com.x0.newsapi.data.local.NewsApiDatabase
import com.x0.newsapi.data.local.SourcesDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
@Singleton
class RoomModule {

    @Provides
    @Singleton
    fun provideDatabase(context: Context): NewsApiDatabase =
        Room.databaseBuilder(context, NewsApiDatabase::class.java, "NewsApi.db").build()

    @Provides
    @Singleton
    fun provideOrdersDao(database: NewsApiDatabase): SourcesDao = database.sourcesDao()
}