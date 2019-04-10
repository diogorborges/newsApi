package com.x0.newsapi.data.model.sources

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sources")
data class Source(
    @PrimaryKey @ColumnInfo(name = "id") val id: String,
    val name: String,
    val description: String,
    val url: String,
    val category: String,
    val language: String,
    val country: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeString(url)
        parcel.writeString(category)
        parcel.writeString(language)
        parcel.writeString(country)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Source> {
        override fun createFromParcel(parcel: Parcel): Source = Source(parcel)

        override fun newArray(size: Int): Array<Source?> = arrayOfNulls(size)
    }
}
