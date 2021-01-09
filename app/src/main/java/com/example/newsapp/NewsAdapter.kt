package com.example.newsapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.newsapp.domain.Article
import org.joda.time.format.DateTimeFormat


class NewsAdapter (private val mArticles: List<Article>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>()
{
    interface OnItemClickListener {
        fun onItemClick(itemView: View?, position: Int)
    }

    private var listener: OnItemClickListener? = null

    // Define the method that allows the parent activity or fragment to define the listener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView), View.OnClickListener {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row

        val titleTextView: TextView = itemView.findViewById(R.id.title)
        val descriptionTextView: TextView= itemView.findViewById(R.id.description)
        val sourceTextView: TextView= itemView.findViewById(R.id.source)
        val datetimeTextView: TextView= itemView.findViewById(R.id.datetime)
        val imageView: ImageView= itemView.findViewById(R.id.imageView)

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(itemView: View?) {
            if (listener != null) {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    listener!!.onItemClick(itemView, position)
                }
            }
        }

    }

    // ... constructor and member variables
    // Usually involves inflating a layout from XML and returning the holder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.item_news_larger, parent, false)
        return ViewHolder(contactView)
    }

    // Involves populating data into the item through holder
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        // Get the data model based on position
        val article: Article = mArticles.get(position)
        // Set item views based on your views and data model
        val titleTextView = viewHolder.titleTextView
        titleTextView.text = article.title

        val descriptionTextView = viewHolder.descriptionTextView
        descriptionTextView.text = article.description

        val sourceTextView = viewHolder.sourceTextView
        sourceTextView.text = article.source?.name

        val datetimeTextView = viewHolder.datetimeTextView
        datetimeTextView.text = article.datetime?.toString(DateTimeFormat.shortDateTime())

        val imageView = viewHolder.imageView

        val displayMetrics = viewHolder.itemView.resources.displayMetrics

        Glide.with(viewHolder.itemView)
            .load(article.urlToImage)
            .error(R.drawable.ic_baseline_broken_image_100)
            .placeholder(R.drawable.ic_baseline_wallpaper_24)
            .override(displayMetrics.widthPixels,
                displayMetrics.heightPixels/2
            )
            .fitCenter()
            .into(imageView)
    }

    // Returns the total count of items in the list
    override fun getItemCount(): Int {
        return mArticles.size
    }

    fun getItems() : List<Article>
    {
        return this.mArticles
    }
}