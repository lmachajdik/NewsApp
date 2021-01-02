package com.example.newsapp.news

import android.os.Parcel
import android.os.Parcelable
import com.squareup.moshi.Json
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
/*
@Database(entities = [Article::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}

@Dao
interface UserDao {
    @Query("SELECT * FROM articles")
    fun getAll(): List<Article>

    @Query("SELECT * FROM articles WHERE category=:Category")
    fun getAllByCategory(Category:String) : List<Article>

    @Insert
    fun insert(vararg users: Article)

    @Insert
    fun insertAll(users: List<Article>)
    {
       users.forEach {
           insert(it)
       }
    }

    @Query("DELETE FROM articles")
    fun deleteAll()

    @Query("DELETE FROM articles WHERE category=:category")
    fun deleteAll(category: String?)
}*/

data class Source(
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
    @Json(name="source")
    var source: Source? = null,
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

