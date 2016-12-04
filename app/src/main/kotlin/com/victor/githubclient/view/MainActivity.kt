package com.victor.githubclient.view

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.victor.githubclient.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState != null) {
            return
        }

        startFragment(RepositoryListFragment())
    }

    fun startFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .add(R.id.maincontainer, fragment, null)
                .commit()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
        }
        return true
    }
}
