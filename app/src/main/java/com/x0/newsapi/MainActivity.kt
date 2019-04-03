package com.x0.newsapi

import android.app.FragmentManager
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_container.toolbar
import kotlinx.android.synthetic.main.activity_main.navigation

class MainActivity : AppCompatActivity(),
    BottomNavigationView.OnNavigationItemSelectedListener,
    BottomNavigationView.OnNavigationItemReselectedListener {

    private var title: String = SourcesFragment.TITLE
    private lateinit var fragment: Fragment

    companion object {
        private const val FRAGMENT_KEY: String = "fragment"
        private const val TITLE_KEY: String = "title"
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        navigation.setOnNavigationItemSelectedListener(this)
        navigation.setOnNavigationItemReselectedListener(this)

        title = savedInstanceState?.getString(TITLE_KEY) ?: title

        if (supportFragmentManager.findFragmentByTag(FRAGMENT_KEY) == null) {
            changeFragment(SourcesFragment())
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.navigation_sources -> {
                title = SourcesFragment.TITLE
                fragment = SourcesFragment()
            }
            R.id.navigation_search -> {
                title = SearchFragment.TITLE
                fragment = SearchFragment()
            }
            R.id.navigation_favorites -> {
                title = FavoritesFragment.TITLE
                fragment = FavoritesFragment()
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
            .popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)

        supportFragmentManager
            .beginTransaction()
            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
            .replace(R.id.main_container, fragment, FRAGMENT_KEY)
            .commit()
    }

    fun showToolbar(status: Boolean) = with(toolbar) {
        when (status) {
            true -> visible()
            else -> gone()
        }
    }

    override fun onNavigationItemReselected(item: MenuItem) {
        Log.i(TAG, "onNavigationItemReselected()");
    }
}

