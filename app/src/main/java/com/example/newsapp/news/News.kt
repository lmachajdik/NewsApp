package com.example.newsapp.news

import android.os.Parcel
import android.os.Parcelable
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

data class Source(var id:String?=null , var name:String?=null) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(name)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Source> {
        override fun createFromParcel(parcel: Parcel): Source {
            return Source(parcel)
        }

        override fun newArray(size: Int): Array<Source?> {
            return arrayOfNulls(size)
        }
    }
}

data class Article(
    var source: Source? = null,
    var author: String? = null,
    var title: String? = null,
    var description: String? = null,
    var content: String?=null,
    var publishedAt : String?=null,
    var url : String?=null,
    var urlToImage : String?=null
    //
) : Parcelable
{

    val datetime: DateTime?
    get(){
        if(publishedAt == null) return null
        return DateTime.parse(
            publishedAt,
            DateTimeFormat.forPattern(DatetimePattern)
        )
    }

    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Source::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(source, flags)
        parcel.writeString(author)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(content)
        parcel.writeString(publishedAt)
        parcel.writeString(url)
        parcel.writeString(urlToImage)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Article> {
        override fun createFromParcel(parcel: Parcel): Article {
            return Article(parcel)
        }

        override fun newArray(size: Int): Array<Article?> {
            return arrayOfNulls(size)
        }

        const val DatetimePattern = "yyyy-MM-dd'T'HH:mm:ssZ"
    }

}

