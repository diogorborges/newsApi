package com.x0.newsapi.data.model.sources

import android.os.Parcel
import android.os.Parcelable

data class SourcesResponse(val status: String, val sources: ArrayList<Source>) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        arrayListOf<Source>().apply {
            parcel.readList(this, Source::class.java.classLoader)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(status)
        parcel.writeTypedList(sources)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<SourcesResponse> {
        override fun createFromParcel(parcel: Parcel): SourcesResponse {
            return SourcesResponse(parcel)
        }

        override fun newArray(size: Int): Array<SourcesResponse?> {
            return arrayOfNulls(size)
        }
    }

}
