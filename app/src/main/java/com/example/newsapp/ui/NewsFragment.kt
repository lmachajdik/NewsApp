package com.example.newsapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.news.Article
import com.example.newsapp.news.NewsAPI
import com.example.newsapp.news.Source
import com.example.newsapp.news.TopHeadlinesResult

/**
 * A fragment representing a list of Items.
 */
class NewsFragment : Fragment() {

    private var columnCount = 1
    private lateinit var list: RecyclerView
    private lateinit var mAdapter : NewsAdapter

    private val useDummyData = true

    fun setDummyData()
    {
        var headlines : TopHeadlinesResult = TopHeadlinesResult()
        headlines.articles = ArrayList()

        val strings = arrayOf(
            "invalid url",
            "https://www.gannett-cdn.com/presto/2020/12/23/USAT/ebfbf052-9030-4f19-af89-841dc435c9bd-WW84-09854r.jpg?crop=2279,1282,x0,y0&width=1600&height=800&fit=bounds",
            "https://cdn.cnn.com/cnnnext/dam/assets/201222070325-02-us-capitol-1221-super-tease.jpg",
            "https://image.cnbcfm.com/api/v1/image/106815140-1608664034954-ubs_weekly_restaurant_revenue.PNG?v=1608664055",
            "https://thehill.com/sites/default/files/ca_vaccinepollwillingness_092220getty_6.jpg",
            "https://imagez.tmz.com/image/c3/16by9/2020/12/27/c379ca133a914fa7b8af8d4c9e7c1133_xl.jpg",
            "https://www.gannett-cdn.com/presto/2020/12/27/USAT/ebfb5d0f-d98f-40ee-a919-a3b84dd66128-AP_Biden_1.jpg?crop=3689,2075,x0,y385&width=3200&height=1680&fit=bounds",
            "https://cdn.cnn.com/cnnnext/dam/assets/201227072434-01-pope-county-arkansas-murders-super-tease.jpg"
        )

        for (u in strings) {


            var a = Article(
                Source("", "Lifehacker.com"),
                "Mike Winters on Two Cents, shared by Mike Winters to Lifehacker",
                "Is the New Visa Bitcoin Rewards Card Worth It?",
                "Visa has partnered with cryptocurrency startup BlockFi to offer the first rewards credit card that pays out in Bitcoin rather than cash, but is it worth applying for? Unless you’re extremely bullish on cryptocurrency and don’t mind getting seriously dinged fo…",
                "Content and moreee.. to fill some space like content would"
            )
            a.urlToImage = u
                //"https://g.foolcdn.com/editorial/images/605979/family-watching-tv-getty-6217.jpg"
            a.url =
                "https://www.fool.com/investing/2020/12/26/got-3000-these-3-tech-stocks-could-make-you-rich-i/"
            headlines.articles?.add(a)
        }
        mAdapter = headlines.articles?.let { NewsAdapter(it) }!!
        list.adapter = mAdapter
    }

    fun fetchNewsFromApi()
    {
        NewsAPI.GetTopHeadlines(
            NewsAPI.Countries.Slovakia,
            NewsAPI.Categories.Science, object : NewsAPI.ReturnCallback {
                override fun callback(headlines: TopHeadlinesResult?) {
                    if (headlines != null) {

                        mAdapter = headlines.articles?.let { NewsAdapter(it) }!!
                        list.adapter = mAdapter
                    }
                }

            }
        )
    }

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
        list = view.findViewById(R.id.list)

        if (savedInstanceState != null) {
            val arr: ArrayList<Article> = savedInstanceState.getParcelableArrayList(ITEMS_KEY)!!
            mAdapter = NewsAdapter(arr)
            list.adapter = mAdapter
        } else if (view is RecyclerView) {

            if(useDummyData)
                setDummyData()
            else
                fetchNewsFromApi()

            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
            }
        }
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState != null) {
            //Restore the fragment's state here
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(ITEMS_KEY, mAdapter.getItems())
    }

    companion object {
        const val ITEMS_KEY = "list"
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