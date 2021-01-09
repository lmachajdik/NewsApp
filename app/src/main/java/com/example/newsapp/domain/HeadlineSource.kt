package com.example.newsapp.domain

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json

data class HeadlineSource(
    @Json(name="id")
    var id:String?=null,
    @Json(name="name")
    var name:String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString().toString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR :
        Parcelable.Creator<HeadlineSource> {
        override fun createFromParcel(parcel: Parcel): HeadlineSource {
            return HeadlineSource(parcel)
        }

        override fun newArray(size: Int): Array<HeadlineSource?> {
            return arrayOfNulls(size)
        }
    }
}