package com.example.newsapp.ui.TopHeadlinesFragments

//import com.example.newsapp.database.NewsDB
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration
import androidx.recyclerview.widget.SnapHelper
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.newsapp.NewsAdapter
import com.example.newsapp.R
import com.example.newsapp.domain.Article
import com.example.newsapp.domain.SharedViewModel
import com.example.newsapp.network.NewsAPI
import com.example.newsapp.repository.HeadlinesRepository
import jp.wasabeef.recyclerview.animators.SlideInUpAnimator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * A fragment representing a list of Items.
 */
abstract class NewsFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private var columnCount = 1
    private lateinit var list: RecyclerView
    private var mAdapter : NewsAdapter = NewsAdapter(ArrayList())
    private lateinit var mSwipeRefreshLayout : SwipeRefreshLayout
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

    fun updateDataFromRepository()
    {
        val job = GlobalScope.launch {
            var a =HeadlinesRepository.getTopHeadlines(NewsAPI.NewsCountry,newsCategory)

            withContext(Dispatchers.Main) {
                a.observe(viewLifecycleOwner) {
                var arr = model.topHeadlines.get(newsCategory.name)?.value
                if (arr != it)
                    model.topHeadlines.get(newsCategory.name)?.value = it

                mSwipeRefreshLayout.isRefreshing = false
                }
            }
        }

        object : CountDownTimer(3000, 500) {
            override fun onFinish() {
                if(mSwipeRefreshLayout.isRefreshing) { //if swipe is still refreshing, fetching data was not completed yet, but it timed out nevertheless
                    mSwipeRefreshLayout.isRefreshing = false

                    Toast.makeText(
                        context,
                        "There was a problem connecting to network",
                        Toast.LENGTH_LONG
                    ).show()
                    job.cancel()
                }
            }

            override fun onTick(p0: Long) {
                if(!mSwipeRefreshLayout.isRefreshing) { //if swipe is not refreshing, fetching data finished and we can cancel timer
                    this.cancel()
                }
            }
        }.start()
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
        setHasOptionsMenu(true)
        list = view.findViewById(R.id.list)

        currentInstance = this

        val snapHelper: SnapHelper = LinearSnapHelper()
        snapHelper.attachToRecyclerView(list)

        list.itemAnimator = SlideInUpAnimator()
        list.adapter = mAdapter

        model = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        if(!model.topHeadlines.containsKey(newsCategory.name)) { //model doesnt contain this fragment's category, initialize it empty
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

        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container) as SwipeRefreshLayout
        mSwipeRefreshLayout.setOnRefreshListener(this)
        mSwipeRefreshLayout.setColorSchemeResources(
            R.color.colorPrimary,
            android.R.color.holo_green_dark,
            android.R.color.holo_orange_dark,
            android.R.color.holo_blue_dark
        )

        mSwipeRefreshLayout.post {
            mSwipeRefreshLayout.isRefreshing = true

            // Fetching data from server
            updateDataFromRepository()
        }

        return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.main, menu)
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

    override fun onRefresh() {
        updateDataFromRepository()
    }
}