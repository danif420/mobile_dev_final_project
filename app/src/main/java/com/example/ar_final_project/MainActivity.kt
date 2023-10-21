package com.example.ar_final_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val login=findViewById<TextView>(R.id.login)
        val about=findViewById<TextView>(R.id.about)
        login.setOnClickListener {
            val intent= Intent(this,Login::class.java)
            startActivity(intent)
        }
        about.setOnClickListener {
            val intent= Intent(this,About::class.java)
            startActivity(intent)
        }
    }
}