package com.example.newsapp

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.newsapp.news.NewsAPI
import com.example.newsapp.news.TopHeadlinesResult
import com.example.newsapp.ui.home.HomeFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import net.danlew.android.joda.JodaTimeAndroid


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        JodaTimeAndroid.init(this)
        NewsAPI.setApiContext(this)

        val fab: FloatingActionButton = findViewById(R.id.fab)
        fab.setOnClickListener { view ->

                NewsAPI.GetTopHeadlines(NewsAPI.Countries.Slovakia,NewsAPI.Categories.Science, object: NewsAPI.ReturnCallback{
                    override fun callback(headlines: TopHeadlinesResult?) {
                        println(headlines)
                    }

                }
                )
        }
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        navView.setItemBackgroundResource(R.drawable.menubackground)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(setOf(
            R.id.nav_news_business,
            R.id.nav_news_entertainment,
            R.id.nav_news_general,
            R.id.nav_news_health,
            R.id.nav_news_science,
            R.id.nav_news_sports,
            R.id.nav_news_technology,
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow), drawerLayout)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private val listener = NavController.OnDestinationChangedListener { controller, destination, arguments ->
/*
        if(destination.id==R.id.nav_home) {

            if (controller.popBackStack(R.id.nav_home, false)) {
                Log.d("", "SettingsFragment found in backStack")
            } else {
                Log.d("", "SettingsFragment not found in backStack, navigate manually")
                controller.navigate(R.id.nav_home)
            }
        }*/
    }

    override fun onResume() {
        super.onResume()
        val navController = findNavController(R.id.nav_host_fragment)
        navController.addOnDestinationChangedListener(listener)
    }

    override fun onPause() {
        val navController = findNavController(R.id.nav_host_fragment)
        navController.removeOnDestinationChangedListener(listener)
        super.onPause()
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return super.onContextItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}