package com.example.newsapp

//import com.example.newsapp.news.AppDatabase
import android.content.DialogInterface
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.View.OnClickListener
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.database.NewsDB
import com.example.newsapp.network.NewsAPI
import com.example.newsapp.ui.TopHeadlinesFragments.NewsFragment
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.danlew.android.joda.JodaTimeAndroid

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)

        JodaTimeAndroid.init(this)
        NewsDB.init(this)
        setSupportActionBar(toolbar)

        toolbar.setOnMenuItemClickListener { it: MenuItem? ->
            if(it?.itemId == R.id.newsCountry_settings)
            {
                val builder: AlertDialog.Builder = this.let {
                    AlertDialog.Builder(it)
                }

                if (builder != null) {
                    var arr = NewsAPI.Countries.values()
                    val items =arrayOfNulls<String>(arr.count())
                    var i = 0
                    arr.forEach {
                        items[i] = arr[i++].name.replace('_',' ')
                    }
                    builder
                        .setTitle("Select Country")
                        .setItems(items, DialogInterface.OnClickListener { dialogInterface, i ->
                            NewsAPI.NewsCountry = NewsAPI.Countries.valueOf(arr[i].name)
                            NewsFragment.currentInstance?.updateDataFromRepository() //fetch data from repository for currently selected country
                        })

                    builder.create().show()
                }
            }
            return@setOnMenuItemClickListener true
        }

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_news_business,
            R.id.nav_news_entertainment,
            R.id.nav_news_general,
            R.id.nav_news_health,
            R.id.nav_news_mixed,
            R.id.nav_news_science,
            R.id.nav_news_sports,
            R.id.nav_news_technology,

            R.id.nav_home,
            R.id.nav_search), drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

   /* override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
       // menuInflater.inflate(R.menu.main, menu)
        return true
    }*/

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}