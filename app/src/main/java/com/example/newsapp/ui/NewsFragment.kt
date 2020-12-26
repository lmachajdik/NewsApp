package com.example.newsapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.newsapp.R
import com.example.newsapp.news.Article
import com.example.newsapp.news.NewsAPI
import com.example.newsapp.news.Source
import com.example.newsapp.news.TopHeadlinesResult
import com.example.newsapp.ui.dummy.DummyContent

/**
 * A fragment representing a list of Items.
 */
class NewsFragment : Fragment() {

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news_list, container, false)

        // Set the adapter
        if (view is RecyclerView) {
            val list: RecyclerView = view.findViewById(R.id.list)
            var headlines : TopHeadlinesResult = TopHeadlinesResult()
            headlines.articles = ArrayList()
            var a = Article(Source("","TestNews Co."), "Author", "Title", "The Saints gained 36 first downs in their Christmas Day beatdown of the Vikings, and what was most remarkable was how easy it was for them to convert: The Saints rarely even got to third down, converting 30 of their 36 first downs on first or second down. Sai…", "Content and moreee.. to fill some space like content would")
            a.urlToImage= "https://g.foolcdn.com/editorial/images/605979/family-watching-tv-getty-6217.jpg"
            a.url =  "https://www.fool.com/investing/2020/12/26/got-3000-these-3-tech-stocks-could-make-you-rich-i/"
            headlines.articles?.add(a)
            list.adapter = headlines.articles?.let { NewsAdapter(it) }
            /*var na: NewsAPI = NewsAPI()
            na.GetTopHeadlines(
                NewsAPI.Companion.Countries.Slovakia,
                NewsAPI.Companion.Categories.Science, object: NewsAPI.ReturnCallback{
                    override fun callback(headlines: TopHeadlinesResult?) {
                        if (headlines != null) {
                            list.adapter = headlines.articles?.let { NewsAdapter(it) }
                        }
                    }

                }
            )*/
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }

                //adapter = NewsAdapter(DummyContent.ITEMS)
            }
        }
        return view
    }

    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            NewsFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }
}