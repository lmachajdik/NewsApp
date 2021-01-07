package com.example.newsapp.ui.SearchFragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.SearchView
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.NewsAdapter
import com.example.newsapp.R
import com.example.newsapp.network.NewsAPI
import com.example.newsapp.repository.HeadlinesRepository


class SearchFragment : Fragment() {



    private lateinit var homeViewModel: HomeViewModel

    private var mAdapter = NewsAdapter(ArrayList())

    private lateinit var list: RecyclerView
    private lateinit var searchOptions : LinearLayout
    private lateinit var sortByRadioGroup : RadioGroup
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel::class.java)
        setHasOptionsMenu(true)
        val root = inflater.inflate(R.layout.fragment_search, container, false)
        list = root.findViewById(R.id.search_recyclerView)
        searchOptions = root.findViewById(R.id.searchOptions_layout)
        sortByRadioGroup = root.findViewById(R.id.sortBy_radioGroup)
        return root
    }

    lateinit var searchView: SearchView
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu?.findItem(R.id.search)
        searchView = searchItem?.actionView as SearchView
        searchView.isIconifiedByDefault = false
        val onClick = { query : String ->
             if(query.count() != 0) {
                 searchOptions.visibility = View.GONE

                 val radioButton: View = sortByRadioGroup.findViewById(sortByRadioGroup.checkedRadioButtonId)
                 val idx: Int = sortByRadioGroup.indexOfChild(radioButton)
                 var sortBy = NewsAPI.SortBy.values()[idx].apiName

                var data= HeadlinesRepository.findHeadlinesFromNetwork(query, sortBy)
                data.observe(viewLifecycleOwner) {
                    searchView.isIconified = false;
                    mAdapter = NewsAdapter(it)
                    mAdapter.setOnItemClickListener(object :
                        NewsAdapter.OnItemClickListener {
                        override fun onItemClick(itemView: View?, position: Int) {
                            var items = mAdapter.getItems()
                            var url = items[position].url
                            if (url != null && url.isNotEmpty()) {
                                val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                startActivity(browserIntent)
                            }
                        }
                    })

                    mAdapter.notifyDataSetChanged()
                    list.adapter = mAdapter
                }
            }
        }
        val queryTextListener: SearchView.OnQueryTextListener =
            object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(s: String): Boolean {
                    if(s.count() != 0) {
                        onClick(s)
                    }
                    return true
                }

                override fun onQueryTextChange(s: String): Boolean {
                    return false
                }
            }

        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener
        {
            override fun onMenuItemActionExpand(p0: MenuItem?): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean {
                searchOptions.visibility = View.VISIBLE
                return true
            }

        })

        searchView.setOnQueryTextListener(queryTextListener) //keyboard search key press
        searchView.setOnClickListener { //menu item search icon in toolbar
            var query = searchView.query.toString();
            if(query.count() != 0)
                onClick(query)
        }
    }
}