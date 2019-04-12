package com.x0.newsapi.presentation

import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.x0.newsapi.NewsApiApplication
import com.x0.newsapi.presentation.news.NewsFragment
import com.x0.newsapi.presentation.sources.SourcesFragment
import kotlinx.android.synthetic.main.activity_container.toolbar
import kotlinx.android.synthetic.main.activity_main.navigation

class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener {

    private var title: String = SourcesFragment.TITLE
    private lateinit var fragment: Fragment

    companion object {
        const val FRAGMENT_KEY: String = "fragment"
        private const val TITLE_KEY: String = "title"
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        NewsApiApplication.instance.applicationComponent.inject(this)

        setContentView(com.x0.newsapi.R.layout.activity_main)
        setSupportActionBar(toolbar)

        toolbar.setNavigationOnClickListener { onBackPressed() }
        navigation.setOnNavigationItemSelectedListener(this)

        title = savedInstanceState?.getString(TITLE_KEY) ?: title

        if (supportFragmentManager.findFragmentByTag(FRAGMENT_KEY) == null) {
            changeFragment(SourcesFragment())
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            com.x0.newsapi.R.id.navigation_sources -> {
                title = SourcesFragment.TITLE
                fragment = SourcesFragment()
            }
            com.x0.newsapi.R.id.navigation_news -> {
                title = NewsFragment.TITLE
                fragment = NewsFragment()
            }
        }

        toolbar.title = title
        changeFragment(fragment)

        return true
    }

    override fun onResume() {
        super.onResume()
        toolbar.title = title
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(
                com.x0.newsapi.R.id.main_container, fragment,
                FRAGMENT_KEY
            )
            .commit()
    }

    fun showBackButton(show: Boolean) = with(toolbar) {
        when (show) {
            true -> navigationIcon =
                resources.getDrawable(com.x0.newsapi.R.drawable.ic_apps_white_24dp)
            else -> navigationIcon = null
        }
    }
}