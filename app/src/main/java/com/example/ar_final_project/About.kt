package com.example.ar_final_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView

class About : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        val home=findViewById<TextView>(R.id.home)
        val login=findViewById<TextView>(R.id.login)
        home.setOnClickListener {
            val intent= Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
        login.setOnClickListener {
            val intent= Intent(this,Login::class.java)
            startActivity(intent)
        }
    }
}