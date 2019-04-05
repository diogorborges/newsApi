package com.x0.newsapi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.x0.newsapi.data.model.sources.Source
import io.reactivex.Single

@Dao
interface SourcesDao {
    @Query("SELECT * from sources WHERE isFavorite =:isFavorite")
    fun getFavoriteSources(isFavorite: Boolean): Single<ArrayList<Source>>

    @Query("UPDATE sources SET isFavorite =:isFavorite WHERE id =:sourceId")
    fun updateFavoriteSource(sourceId: String, isFavorite: Boolean)

    @Query("SELECT * from sources")
    fun getSources(): Single<ArrayList<Source>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSources(vararg sources: Source)

    @Query("DELETE FROM sources")
    fun deleteSources()
}
