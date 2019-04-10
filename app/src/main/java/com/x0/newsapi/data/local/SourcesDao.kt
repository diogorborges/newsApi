package com.x0.newsapi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.x0.newsapi.data.model.sources.Source
import io.reactivex.Single

@Dao
interface SourcesDao {
    @Query("SELECT * from sources")
    fun getSources(): Single<List<Source>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSources(vararg sources: Source)
}
