package com.example.ar_final_project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.fragment.app.Fragment
import com.example.ar_final_project.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_main)
        setContentView(binding.root)
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener(bottomNavListener)
        replaceFragment(Home())
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
                val bundle = Bundle()
                bundle.putString("username", intent.getStringExtra("username"))
                bundle.putString("token", intent.getStringExtra("token"))
                val accountFragment = Account()
                accountFragment.arguments = bundle
                replaceFragment(accountFragment)
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