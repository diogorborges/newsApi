package com.x0.newsapi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.x0.newsapi.data.model.news.Article
import com.x0.newsapi.data.model.sources.Source

@Database(entities = [Source::class, Article::class], version = 1, exportSchema = false)
@TypeConverters(Article.ArticleSource::class)
abstract class NewsApiDatabase : RoomDatabase() {
    abstract fun sourcesDao(): SourcesDao

    abstract fun newsDao(): NewsDao
}