package com.example.ar_final_project

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val blogin=findViewById<AppCompatButton>(R.id.blogin)
        val uname=findViewById<EditText>(R.id.username)
        val caccount=findViewById<TextView>(R.id.create_account)
        val upass=findViewById<EditText>(R.id.userpass)
        blogin.setOnClickListener {
            var name = uname.text.toString()
            var pass = upass.text.toString()
            if (name.isNotEmpty() && pass.isNotEmpty()) {
                val service = RetrofitService.RetrofitServiceFactory.makeRetrofitService()
                lifecycleScope.launch {
                    try {
                        val response = service.login(name, pass)
                            // Successful login (HTTP 2xx)
                        response?.let {
                            if (response.str == "success") {
                                // Navigate to the next activity upon successful login
                                val intent = Intent(this@Login, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                // Handle other cases based on the response content
                                Toast.makeText(
                                    this@Login,
                                    "Login failed: ${response.str}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } catch (e: Exception) {
                        // Handle network or other exceptions
                        Toast.makeText(
                            this@Login,
                            "Login failed: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }
        caccount.setOnClickListener {
            val intent = Intent(this@Login, CreateUser::class.java)
            startActivity(intent)
        }
    }
}