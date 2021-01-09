package com.example.newsapp.ui.home

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.R
import com.example.newsapp.ui.SearchFragment.SearchViewModel


class HomeFragment : Fragment(), HomeItemAdapter.OnItemClickListener{
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: HomeItemAdapter

    private lateinit var searchViewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        searchViewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)
        val logo: ImageView = view.findViewById(R.id.home_logo)
        logo.setOnClickListener{
            Toast.makeText(context, "Author: Lukáš Machajdík", Toast.LENGTH_LONG).show()
        }

        val search : ImageView = view.findViewById(R.id.home_search)
        search.setOnClickListener { val searchEditText = view.findViewById<EditText>(R.id.home_search_editText)
            searchViewModel.setQuery(searchEditText.editableText.toString())

            val navController = findNavController()
            navController.navigate(R.id.nav_search)
        }

        recyclerView = view.findViewById(R.id.home_list)
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        recyclerView.isNestedScrollingEnabled = false
        val orientation = resources.configuration.orientation

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            logo.visibility = View.GONE
            val cLayout : ConstraintLayout = view.findViewById(R.id.constraintLayout)
            val layoutParams: ConstraintLayout.LayoutParams = cLayout.layoutParams as ConstraintLayout.LayoutParams
            layoutParams.leftToLeft = view.id
            layoutParams.rightToRight = view.id
            layoutParams.topToTop = view.id
            layoutParams.bottomToBottom = view.id
            layoutParams.startToStart = view.id
            layoutParams.endToEnd = view.id
            cLayout.layoutParams = layoutParams

        } else {
            logo.visibility = View.VISIBLE
        }

        val items: ArrayList<HomeItem> = ArrayList()

        items.add(HomeItem(R.id.nav_news_business, R.drawable.ic_baseline_business_center_24,getString(R.string.fragment_title_business)))
        items.add(HomeItem(R.id.nav_news_entertainment, R.drawable.ic_baseline_sports_esports_24,getString(R.string.fragment_title_entertainment)))
        items.add(HomeItem(R.id.nav_news_general, R.drawable.ic_baseline_forum_24,getString(R.string.fragment_title_general)))
        items.add(HomeItem(R.id.nav_news_health, R.drawable.ic_baseline_local_hospital_24,getString(R.string.fragment_title_health)))
        items.add(HomeItem(R.id.nav_news_mixed, R.drawable.ic_baseline_dynamic_feed_24,getString(R.string.fragment_title_mixed)))
        items.add(HomeItem(R.id.nav_news_science, R.drawable.ic_baseline_insert_chart_outlined_24,getString(R.string.fragment_title_science)))
        items.add(HomeItem(R.id.nav_news_sports, R.drawable.ic_baseline_sports_24,getString(R.string.fragment_title_sports)))
        items.add(HomeItem(R.id.nav_news_technology, R.drawable.ic_baseline_memory_24,getString(R.string.fragment_title_technology)))

        adapter = HomeItemAdapter(items)
        adapter.setOnItemClickListener(this)
        recyclerView.adapter = adapter

        return view
    }

    override fun onItemClick(itemView: View?, position: Int) {
        val navController = findNavController()
        navController.navigate(adapter.getItem(position).fragmentRes)
    }
}