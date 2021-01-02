package com.example.newsapp.NewsFragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.SnapHelper
import com.example.newsapp.NewsDB
import com.example.newsapp.R
import com.example.newsapp.news.*
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.ArrayList


class SharedViewModel : ViewModel() {
    var news : ArrayList<Article> = ArrayList()
    var topHeadlines =Hashtable<String, ArrayList<Article>>()
}
/**
 * A fragment representing a list of Items.
 */
abstract class NewsFragment : Fragment() {
    private var columnCount = 1
    private lateinit var list: RecyclerView
    private lateinit var mAdapter : NewsAdapter

    protected var newsCategory = NewsAPI.Categories.None

    var UpdateNeeded = false

    private var _newsLanguage: NewsAPI.Countries = NewsAPI.Countries.Slovakia
    var NewsCountry: NewsAPI.Countries
        get() { return _newsLanguage
        }
        set(value){
            _newsLanguage = value
            if(NewsAPI.NewsCountry != _newsLanguage)
                UpdateNeeded = true
        }


    private val useDummyData = false
    private fun getDummyData() : ArrayList<Article>
    {
        var headlines : TopHeadlinesResult =
            TopHeadlinesResult()
        headlines.articles = ArrayList()

        val strings = arrayOf(
            //"invalid url",
            "https://www.gannett-cdn.com/presto/2020/12/23/USAT/ebfbf052-9030-4f19-af89-841dc435c9bd-WW84-09854r.jpg?crop=2279,1282,x0,y0&width=1600&height=800&fit=bounds",
            "https://cdn.cnn.com/cnnnext/dam/assets/201222070325-02-us-capitol-1221-super-tease.jpg",
            "https://image.cnbcfm.com/api/v1/image/106815140-1608664034954-ubs_weekly_restaurant_revenue.PNG?v=1608664055",
            "https://thehill.com/sites/default/files/ca_vaccinepollwillingness_092220getty_6.jpg",
            "https://imagez.tmz.com/image/c3/16by9/2020/12/27/c379ca133a914fa7b8af8d4c9e7c1133_xl.jpg",
            "https://www.gannett-cdn.com/presto/2020/12/27/USAT/ebfb5d0f-d98f-40ee-a919-a3b84dd66128-AP_Biden_1.jpg?crop=3689,2075,x0,y385&width=3200&height=1680&fit=bounds",
            "https://cdn.cnn.com/cnnnext/dam/assets/201227072434-01-pope-county-arkansas-murders-super-tease.jpg"
        )

        var q = 2000;

        for (u in strings) {


            var a = Article(
                null,
                Source(null,"", "Lifehacker.com"),
                "Mike Winters on Two Cents, shared by Mike Winters to Lifehacker",
                "Is the New Visa Bitcoin Rewards Card Worth It?",
                "Visa has partnered with cryptocurrency startup BlockFi to offer the first rewards credit card that pays out in Bitcoin rather than cash, but is it worth applying for? Unless you’re extremely bullish on cryptocurrency and don’t mind getting seriously dinged fo…",
                "Content and moreee.. to fill some space like content would"
            )
            a.urlToImage = u
            //"https://g.foolcdn.com/editorial/images/605979/family-watching-tv-getty-6217.jpg"
            a.url =
                "https://www.fool.com/investing/2020/12/26/got-3000-these-3-tech-stocks-could-make-you-rich-i/"
            a.publishedAt= (++q).toString() + "-12-03T22:00:00Z"
            headlines.articles?.add(a)
        }
        return headlines.articles!!
    }

    fun fetchNewsFromApi(model: SharedViewModel = this.model)
    {
        if(model.topHeadlines.keys.contains(newsCategory.name))
            model.topHeadlines.remove(newsCategory.name)

        NewsAPI.GetTopHeadlines(
            NewsAPI.NewsCountry,
            newsCategory,
            object : NewsAPI.ReturnCallback {
                override fun callback(headlines: TopHeadlinesResult?) {
                    if (headlines != null) {
                        NewsCountry = NewsAPI.NewsCountry

                        activity?.runOnUiThread {
                            model.topHeadlines.put(newsCategory.name, headlines.articles!!)
                            //model.news = headlines.articles!!
                            mAdapter = headlines.articles?.let {
                                NewsAdapter(it)
                            }!!
                            mAdapter.setOnItemClickListener(object :
                                NewsAdapter.OnItemClickListener {
                                override fun onItemClick(itemView: View?, position: Int) {
                                    var items = mAdapter.getItems()
                                    var url = items[position].url
                                    if (url != null && url.isNotEmpty()) {
                                        val browserIntent =
                                            Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                        startActivity(browserIntent)
                                    }
                                }
                            })
                            mAdapter.notifyDataSetChanged()
                            list.adapter = mAdapter
                        }

                        GlobalScope.launch {
                            NewsDB.deleteAllArticles(newsCategory)
                            NewsDB.insertArticles(headlines.articles!!)
                        }
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

    var model : SharedViewModel = SharedViewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_news_list, container, false)
        list = view.findViewById(R.id.list)
        val itemDecoration: ItemDecoration =
            DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        list.addItemDecoration(itemDecoration)

        currentInstance = this

        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(list)

        list.itemAnimator = SlideInUpAnimator()

        model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        if(!model.topHeadlines.containsKey(newsCategory.name))
            model.topHeadlines.put(newsCategory.name, ArrayList())

        if(NewsAPI.NewsCountry != _newsLanguage)
            UpdateNeeded = true

        if(UpdateNeeded) {
            fetchNewsFromApi(model)
        }
        else if (savedInstanceState != null) { //retrieve data from orientation change
            //model.news = savedInstanceState.getParcelableArrayList(ITEMS_KEY)!!
            model.topHeadlines.put(newsCategory.name,savedInstanceState.getParcelableArrayList(
                ITEMS_KEY
            )!!)
        }
        else if(model.topHeadlines.get(newsCategory.name)?.count()  == 0) { //retrieve data from fragment change
            if(useDummyData) {
                model.topHeadlines.put(newsCategory.name,getDummyData())
               // model.news = getDummyData()
            }
            else
                fetchNewsFromApi(model)
        }

        if(!model.topHeadlines.containsKey(newsCategory.name))
            model.topHeadlines.put(newsCategory.name, ArrayList())

        mAdapter =
            NewsAdapter(model.topHeadlines.get(newsCategory.name)!!)

       // mAdapter = NewsAdapter(model.news)
        mAdapter.setOnItemClickListener(object:
            NewsAdapter.OnItemClickListener {
            override fun onItemClick(itemView: View?, position: Int) {
                var items = mAdapter.getItems()
                var url = items[position].url
                if(url != null && url.isNotEmpty()) {
                    val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(browserIntent)
                }
            }
        })
        list.adapter = mAdapter

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(ITEMS_KEY, mAdapter.getItems())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if(currentInstance == this)
            currentInstance = null
    }

    companion object {
        const val ITEMS_KEY = "list"
        const val ARG_COLUMN_COUNT = "column-count"
        var currentInstance: NewsFragment? = null
    }
}