package com.x0.newsapi.data.local

import com.x0.newsapi.data.model.sources.Source

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Source::class], version = 1, exportSchema = false)
abstract class NewsApiDatabase : RoomDatabase() {
    abstract fun sourcesDao(): SourcesDao
}