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
//import com.example.newsapp.database.NewsDB
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

    protected var newsCategory = NewsAPI.Categories.Mixed

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