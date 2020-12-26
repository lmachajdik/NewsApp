package com.example.newsapp.ui

import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.R
import com.example.newsapp.news.Article
import java.net.URL


class NewsAdapter (private val mArticles: ArrayList<Article>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>()
{
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView) {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row
        val titleTextView = itemView.findViewById<TextView>(R.id.title)
        val descriptionTextView = itemView.findViewById<TextView>(R.id.description)
        val sourceTextView = itemView.findViewById<TextView>(R.id.source)
        val authorTextView = itemView.findViewById<TextView>(R.id.author)
        val imageView = itemView.findViewById<ImageView>(R.id.imageView)
    }

    // ... constructor and member variables
    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.item_news, parent, false)
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

        val authorTextView = viewHolder.authorTextView
        authorTextView.text = article.author

        val sourceTextView = viewHolder.sourceTextView
        sourceTextView.text = article.source?.name

        val imageView = viewHolder.imageView
        Glide.with(viewHolder.itemView).load(article.urlToImage).into(imageView);
    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return mArticles.size
    }
}