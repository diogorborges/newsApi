package com.x0.newsapi.data.model.news

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson

@Entity(tableName = "news")
data class Article(
    val source: ArticleSource,
    val author: String?,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage: String,
    val publishedAt: String,
    @PrimaryKey @ColumnInfo(name = "pageNumber") var pageNumber: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        ArticleSource.dataFromString(parcel.readString()),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(ArticleSource.dataToString(source))
        parcel.writeString(author)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(url)
        parcel.writeString(urlToImage)
        parcel.writeString(publishedAt)
    }

    override fun describeContents(): Int = 0

    class ArticleSource {

        lateinit var id: String
        lateinit var name: String

        companion object {

            @JvmStatic
            @TypeConverter
            fun dataToString(data: ArticleSource): String = Gson().toJson(data)

            @JvmStatic
            @TypeConverter
            fun dataFromString(dataJson: String): ArticleSource =
                Gson().fromJson(dataJson, ArticleSource::class.java)
        }
    }

    companion object CREATOR : Parcelable.Creator<Article> {
        override fun createFromParcel(parcel: Parcel): Article =
            Article(parcel)

        override fun newArray(size: Int): Array<Article?> = arrayOfNulls(size)
    }
}
