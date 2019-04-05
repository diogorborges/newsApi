package com.x0.newsapi.data.model.news

import android.os.Parcel
import android.os.Parcelable

data class NewsResponse(
    val status: String,
    val totalResults: Int,
    val articles: ArrayList<Article>
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readInt(),
        arrayListOf<Article>().apply {
            parcel.readList(this, Article::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(status)
        parcel.writeInt(totalResults)
        parcel.writeTypedList(articles)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<NewsResponse> {
        override fun createFromParcel(parcel: Parcel): NewsResponse =
            NewsResponse(parcel)

        override fun newArray(size: Int): Array<NewsResponse?> = arrayOfNulls(size)
    }

}
