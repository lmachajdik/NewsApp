package com.example.newsapp.models

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

data class HeadlineSource(
    @Json(name="id")
    var id:String?=null,
    @Json(name="name")
    var name:String?=null
) : Parcelable {
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

    companion object CREATOR : Parcelable.Creator<HeadlineSource> {
        override fun createFromParcel(parcel: Parcel): HeadlineSource {
            return HeadlineSource(parcel)
        }

        override fun newArray(size: Int): Array<HeadlineSource?> {
            return arrayOfNulls(size)
        }
    }
}


data class Article(
    @Json(name="source")
    var source: HeadlineSource? = null,
    @Json(name="author")
    var author: String? = null,
    @Json(name="title")
    var title: String? = null,
    @Json(name="description")
    var description: String? = null,
    @Json(name="content")
    var content: String?=null,
    @Json(name="publishedAt")
    var publishedAt : String?=null,
    @Json(name="url")
    var url : String?=null,
    @Json(name="urlToImage")
    var urlToImage : String?=null,

    var category: String?=null
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
       // parcel.readInt(),
        parcel.readParcelable(HeadlineSource::class.java.classLoader),
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
       // id?.let { parcel.writeInt(it) }
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

    companion object CREATOR :
        Parcelable.Creator<Article> {
        override fun createFromParcel(parcel: Parcel): Article {
            return Article(parcel)
        }

        override fun newArray(size: Int): Array<Article?> {
            return arrayOfNulls(size)
        }

        const val DatetimePattern = "yyyy-MM-dd'T'HH:mm:ssZ"
    }

}