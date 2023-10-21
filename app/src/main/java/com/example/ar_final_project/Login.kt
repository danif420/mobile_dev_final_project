package com.example.ar_final_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val home=findViewById<TextView>(R.id.home)
        val about=findViewById<TextView>(R.id.about)
        home.setOnClickListener {
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        about.setOnClickListener {
            val intent= Intent(this,About::class.java)
            startActivity(intent)
        }
    }
}