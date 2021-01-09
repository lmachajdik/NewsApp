package com.example.newsapp.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R

data class HomeItem(val fragmentRes: Int, val imgRes: Int, val text: String)

class HomeItemAdapter (private val mItems: List<HomeItem>) : RecyclerView.Adapter<HomeItemAdapter.ViewHolder>()
{
    interface OnItemClickListener {
        fun onItemClick(itemView: View?, position: Int)
    }

    private var listener: OnItemClickListener? = null

    // Define the method that allows the parent activity or fragment to define the listener
    fun setOnItemClickListener(listener: OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(listItemView: View) : RecyclerView.ViewHolder(listItemView), View.OnClickListener {
        // Your holder should contain and initialize a member variable
        // for any view that will be set as you render a row

        val titleTextView: TextView = itemView.findViewById(R.id.home_title)
        val imageView: ImageView = itemView.findViewById(R.id.home_img)

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
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeItemAdapter.ViewHolder {
        val context = parent.context
        val inflater = LayoutInflater.from(context)
        // Inflate the custom layout
        val contactView = inflater.inflate(R.layout.item_home, parent, false)
        return ViewHolder(contactView)
    }

    override fun getItemCount(): Int {
        return mItems.count()
    }

    override fun onBindViewHolder(holder: HomeItemAdapter.ViewHolder, position: Int) {
        val item: HomeItem = mItems.get(position)

        val img = holder.imageView
        val titleTextView = holder.titleTextView
        titleTextView.text = item.text
        img.setImageResource(item.imgRes)
    }

    fun getItem(position: Int) : HomeItem
    {
        return mItems[position]
    }
}