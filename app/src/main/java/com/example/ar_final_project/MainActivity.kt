package com.example.ar_final_project

import Home
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavListener)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        Log.d("TAG", "onCreateOptionsMenu called")
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    private val bottomNavListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.page_home -> {
                replaceFragment(Home())
                return@OnNavigationItemSelectedListener true
            }
            R.id.page_search -> {
                replaceFragment(Search())
                return@OnNavigationItemSelectedListener true
            }
            R.id.page_upload -> {
                replaceFragment(Upload())
                return@OnNavigationItemSelectedListener true
            }
            R.id.page_account -> {
                replaceFragment(Account())
                return@OnNavigationItemSelectedListener true
            }
            R.id.page_about -> {
                replaceFragment(About())
                return@OnNavigationItemSelectedListener true
            }
            else -> return@OnNavigationItemSelectedListener false
        }
    }
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }
}