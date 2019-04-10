package com.x0.newsapi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.x0.newsapi.data.model.news.Article
import io.reactivex.Single

@Dao
interface NewsDao {
    @Query("SELECT * from news")
    fun getNews(): Single<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(vararg news: Article)

    @Query("DELETE FROM news")
    fun deleteNews()
}