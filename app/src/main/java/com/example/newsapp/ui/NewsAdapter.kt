package com.example.newsapp.ui

import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.Target
import com.example.newsapp.R
import com.example.newsapp.news.Article
import kotlin.math.roundToInt


class NewsAdapter (private val mArticles: ArrayList<Article>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>()
{
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val titleTextView: TextView = itemView.findViewById(R.id.title)
        val descriptionTextView: TextView = itemView.findViewById(R.id.description)
        val sourceTextView: TextView = itemView.findViewById(R.id.source)
        //val authorTextView = itemView.findViewById<TextView>(R.id.author)
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

    // ... constructor and member variables
    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.item_news_larger, parent, false)
        // Return a new holder instance
        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: NewsAdapter.ViewHolder, position: Int) {
        // Get the data model based on position
        val article: Article = mArticles.get(position)
        // Set item views based on your views and data model
        val titleTextView = viewHolder.titleTextView
        titleTextView.setText(article.title)

        val descriptionTextView = viewHolder.descriptionTextView
        descriptionTextView.text = article.description

        //val authorTextView = viewHolder.authorTextView
        //authorTextView.text = article.author

        val sourceTextView = viewHolder.sourceTextView
        sourceTextView.text = article.source?.name

        val displayMetrics = DisplayMetrics()

        val height = displayMetrics.heightPixels
        val width = displayMetrics.widthPixels

        val imageView = viewHolder.imageView

        val dip = 200f

        val r: Resources = viewHolder.itemView.resources
        val px = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dip,
            r.getDisplayMetrics()
        )

        Glide.with(viewHolder.itemView)
            .load(article.urlToImage)
           // .error(R.drawable.ic_baseline_memory_24)
            .placeholder(R.drawable.ic_baseline_wallpaper_24)
            .override(Target.SIZE_ORIGINAL, px.roundToInt())
          //  .override(100 , 100)
            .into(imageView);
    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return mArticles.size
    }

    fun getItems() : ArrayList<Article>
    {
        return ArrayList(this.mArticles)
    }
}