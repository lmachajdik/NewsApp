package com.example.newsapp.ui.NewsFragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.SnapHelper
import com.example.newsapp.NewsAdapter
//import com.example.newsapp.NewsDB
import com.example.newsapp.R
import com.example.newsapp.domain.Article
import com.example.newsapp.domain.SharedViewModel
import com.example.newsapp.domain.HeadlineSource
import com.example.newsapp.network.NetworkTopHeadlinesResult
import com.example.newsapp.repository.HeadlinesRepository
import com.example.newsapp.network.NewsAPI
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlin.collections.ArrayList

/**
 * A fragment representing a list of Items.
 */
abstract class NewsFragment : Fragment() {
    private var columnCount = 1
    private lateinit var list: RecyclerView
    private var mAdapter : NewsAdapter = NewsAdapter(ArrayList())

    protected var newsCategory = NewsAPI.Categories.None

    var UpdateNeeded = false

    private var _newsCountry: NewsAPI.Countries = NewsAPI.Countries.Slovakia
    var newsCountry: NewsAPI.Countries
        get() { return _newsCountry
        }
        set(value){
            _newsCountry = value
            if(NewsAPI.NewsCountry != _newsCountry)
                UpdateNeeded = true
        }


    private val useDummyData = false
    private fun getDummyData() : ArrayList<Article>
    {
        var headlines : NetworkTopHeadlinesResult =
            NetworkTopHeadlinesResult()
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
                //    null,
                HeadlineSource("", "Lifehacker.com"),
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

    fun getTopHeadlinesFromRepository()
    {
        HeadlinesRepository.getTopHeadlines(NewsAPI.NewsCountry,newsCategory)
        .observe(viewLifecycleOwner){
            var arr = model.topHeadlines.get(newsCategory.name)?.value
            if(arr != it)
                model.topHeadlines.get(newsCategory.name)?.value =it
            else
                println()
            /*/* var a = model?.topHeadlines?.get(newsCategory.name)?.value
                 if(newsCountry == NewsAPI.NewsCountry &&

                         )*/
                 newsCountry = NewsAPI.NewsCountry

                 activity?.runOnUiThread {
                     model.topHeadlines.get(newsCategory.name)?.value = it
                     mAdapter.notifyDataSetChanged()
                 }

                 GlobalScope.launch {
                     //NewsDB.deleteAllArticles(newsCategory)
                     // NewsDB.insertArticles(headlines.articles!!)
                 }*/
        }
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
        list.adapter = mAdapter

        model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        if(!model.topHeadlines.containsKey(newsCategory.name)) { //model doesnt contain this fragment's category, initialize it
            var mld = MutableLiveData<List<Article>>()
            model.topHeadlines.put(newsCategory.name, mld)
            //model.topHeadlines.get(newsCategory.name)?.value = ArrayList()
        }

        model.topHeadlines[newsCategory.name]?.observe(viewLifecycleOwner) {
            mAdapter = NewsAdapter(it)
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
            mAdapter.notifyDataSetChanged()
            list.adapter = mAdapter
            newsCountry  = NewsAPI.NewsCountry
        }

        if(NewsAPI.NewsCountry != _newsCountry)
            UpdateNeeded = true

        /*if (savedInstanceState != null) { //retrieve data from orientation change
            var arr = (savedInstanceState.get(ITEMS_KEY) as Array<Article>)
            model.topHeadlines[newsCategory.name]?.value = arr.toList()
        }*/

        getTopHeadlinesFromRepository()


        /*else if (savedInstanceState != null) { //retrieve data from orientation change
            var arr = (savedInstanceState.get(ITEMS_KEY) as Array<Article>)
            model.topHeadlines[newsCategory.name]?.value = arr.toList()
        }
        else if(model.topHeadlines.get(newsCategory.name)?.value?.count()  == 0) { //if local model is empty
            if (useDummyData) {
                model.topHeadlines[newsCategory.name]?.value = getDummyData().toList()
            } else
                getTopHeadlinesFromRepository()
        }*/

        return view
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArray(ITEMS_KEY, mAdapter.getItems().toTypedArray())
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