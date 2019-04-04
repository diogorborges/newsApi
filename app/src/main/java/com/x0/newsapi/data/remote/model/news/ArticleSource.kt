package com.x0.newsapi.data.remote.model.news

import android.os.Parcel
import android.os.Parcelable

class ArticleSource(val id: String, val name: String) : Parcelable {
    constructor(parcel: Parcel) : this(parcel.readString(), parcel.readString())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<ArticleSource> {
        override fun createFromParcel(parcel: Parcel): ArticleSource =
            ArticleSource(parcel)

        override fun newArray(size: Int): Array<ArticleSource?> = arrayOfNulls(size)
    }

}
