package com.example.newsapp.ui.SearchFragment

import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import com.example.newsapp.NewsAdapter
import com.example.newsapp.R
import com.example.newsapp.network.NewsAPI
import com.example.newsapp.repository.HeadlinesRepository
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList


class SearchFragment : Fragment() {
    private lateinit var searchViewModel: SearchViewModel
    private var mAdapter = NewsAdapter(ArrayList())

    private lateinit var list: RecyclerView
    private lateinit var searchOptions : LinearLayout
    private lateinit var sortByRadioGroup : RadioGroup
    private lateinit var languageSelectSpinner : Spinner
    private lateinit var fromDate : EditText
    private lateinit var toDate : EditText

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchViewModel = ViewModelProvider(requireActivity()).get(SearchViewModel::class.java)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        if(!searchViewModel.query.value.isNullOrEmpty()) //if got data from home fragment
        {
            val searchItem = menu.findItem(R.id.search)
            searchView = searchItem?.actionView as SearchView

            searchItem.expandActionView()
            searchView.isIconified = false
            searchView.setQuery(searchViewModel.query.value, false)

        }
    }


    lateinit var searchView: SearchView
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)
        val searchItem = menu.findItem(R.id.search)
        searchView = searchItem?.actionView as SearchView
        searchView.isIconified = false

        val onClick = { query : String ->
             if(query.count() != 0) {
                 searchOptions.visibility = View.GONE

                 val radioButton: View = sortByRadioGroup.findViewById(sortByRadioGroup.checkedRadioButtonId)
                 val idx: Int = sortByRadioGroup.indexOfChild(radioButton)
                 val sortBy = NewsAPI.SortBy.values()[idx].apiName
                 var language = NewsAPI.Languages.values().find {
                     it.name == languageSelectSpinner.selectedItem
                 }
                 if(language == null)
                     language = NewsAPI.Languages.English

                 val fromDateStr = fromDate.editableText.toString()
                 val toDateStr = toDate.editableText.toString()

                val data= HeadlinesRepository.findHeadlinesFromNetwork(query, sortBy, language, fromDateStr, toDateStr)
                data.observe(viewLifecycleOwner) {
                    searchView.isIconified = false
                    mAdapter = NewsAdapter(it)
                    mAdapter.setOnItemClickListener(object :
                        NewsAdapter.OnItemClickListener {
                        override fun onItemClick(itemView: View?, position: Int) {
                            val items = mAdapter.getItems()
                            val url = items[position].url
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

            override fun onMenuItemActionCollapse(p0: MenuItem?): Boolean { //on back click
                searchOptions.visibility = View.VISIBLE
                searchView.isIconified = true
                return true
            }

        })

        searchView.setOnQueryTextListener(queryTextListener) //keyboard search key press
        searchView.setOnClickListener { //menu item search icon in toolbar
            val query = searchView.query.toString()
            if(query.count() != 0)
                onClick(query)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        setHasOptionsMenu(true)

        val root = inflater.inflate(R.layout.fragment_search, container, false)
        list = root.findViewById(R.id.search_recyclerView)
        searchOptions = root.findViewById(R.id.searchOptions_layout)
        sortByRadioGroup = root.findViewById(R.id.sortBy_radioGroup)
        fromDate = root.findViewById(R.id.fromDate)
        toDate = root.findViewById(R.id.toDate)

        val items = NewsAPI.Languages.values().map { return@map it.name }

        languageSelectSpinner = root.findViewById(R.id.spinner) as Spinner
        val adapter : ArrayAdapter<String> =
            ArrayAdapter(requireContext(),android.R.layout.simple_spinner_dropdown_item, items)
        languageSelectSpinner.adapter = adapter

        val defaultLanguageIndex= items.indexOf(NewsAPI.Languages.English.name)
        if(defaultLanguageIndex != -1)
            languageSelectSpinner.setSelection(defaultLanguageIndex)

        val onDateClickListener = { it:View ->
            val editText = it as EditText
            val cldr: Calendar = Calendar.getInstance()
            val day: Int = cldr.get(Calendar.DAY_OF_MONTH)
            val month: Int = cldr.get(Calendar.MONTH)
            val year: Int = cldr.get(Calendar.YEAR)
            // date picker dialog
            val picker = DatePickerDialog(
                requireContext(),
                OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    editText.setText(
                        year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth
                    )

                    var fromDateStr = fromDate.editableText.toString()
                    var toDateStr = toDate.editableText.toString()

                    if (!fromDateStr.isBlank() && !toDateStr.isBlank()) {
                        val formatter: DateTimeFormatter =
                            DateTimeFormat.forPattern("yyyy-MM-dd")
                        val dt: DateTime = formatter.parseDateTime(fromDateStr)
                        val dt2 = formatter.parseDateTime(toDateStr)
                        if (dt.isAfter(dt2)) {
                            val tmp = fromDateStr
                            fromDateStr = toDateStr
                            toDateStr = tmp
                            fromDate.setText(fromDateStr)
                            toDate.setText(toDateStr)

                        }
                    }

                }, year, month, day
            )
            picker.setButton(
                DialogInterface.BUTTON_NEGATIVE, "Cancel",
                DialogInterface.OnClickListener { _, which ->
                    if (which == DialogInterface.BUTTON_NEGATIVE) {
                        editText.setText("")
                    }
                })
            picker.show()
        }

        fromDate.setOnClickListener(onDateClickListener)
        toDate.setOnClickListener(onDateClickListener)
        return root
    }

}
