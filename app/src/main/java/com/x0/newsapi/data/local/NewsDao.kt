package com.x0.newsapi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.x0.newsapi.data.model.news.Article
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface NewsDao {
    @Query("SELECT * from news WHERE pageNumber =:pageNumber")
    fun getNews(pageNumber: Int): Single<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNews(vararg news: Article)

    @Query("DELETE FROM news")
    fun deleteNews()

    @Query("UPDATE news SET pageNumber =:nextPage WHERE pageNumber =:previousPage")
    fun updatePageNumber(nextPage: Int, previousPage: Int)
}