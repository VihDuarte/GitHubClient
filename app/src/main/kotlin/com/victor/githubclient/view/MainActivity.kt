package com.victor.githubclient.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.FrameLayout
import com.victor.githubclient.R

class MainActivity : AppCompatActivity(), ContainerView {
    internal var mainContainer: FrameLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mainContainer = findViewById(R.id.maincontainer) as FrameLayout?

        if (savedInstanceState != null) {
            return
        }

        startFragment(RepositoryListFragment())
        supportActionBar?.setTitle(R.string.respository_list_title)

        if (mainContainer == null) {
            showDetail(RepositoryDetailFragment(), "")
        }
    }

    fun startFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .add(if (mainContainer != null) R.id.maincontainer else R.id.repository_list_container, fragment, null)
                .commit()
    }

    override fun showDetail(fragment: Fragment, title: String) {
        supportFragmentManager
                .beginTransaction()
                .replace(if (mainContainer != null) R.id.maincontainer else R.id.repository_detail_container,
                        fragment)
                .addToBackStack(null)
                .commitAllowingStateLoss()

        supportActionBar?.title = title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        super.onBackPressed()

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setTitle(R.string.respository_list_title)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }
}
