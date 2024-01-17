package com.example.ar_final_project

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class CreateUser : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_user)
        val username = findViewById<EditText>(R.id.username)
        val password = findViewById<EditText>(R.id.password)
        val confirm = findViewById<EditText>(R.id.confirm)
        val signup = findViewById<AppCompatButton>(R.id.signupb)

        signup.setOnClickListener {
            val uname = username.text.toString()
            val pass = password.text.toString()
            val conf = confirm.text.toString()
            if (uname.isNotEmpty() && pass.isNotEmpty() && conf.isNotEmpty()) {
                if (pass==conf) {
                    val service = RetrofitService.RetrofitServiceFactory.makeRetrofitService()
                    lifecycleScope.launch {
                        try {
                            service.signup(uname, pass)
                            Toast.makeText(
                                this@CreateUser,
                                "@string/usercreated",
                                Toast.LENGTH_SHORT
                            ).show()
                            val intent = Intent(this@CreateUser, Login::class.java)
                            startActivity(intent)
                            finish()
                        } catch (e: Exception) {
                            // Handle network or other exceptions
                            Toast.makeText(
                                this@CreateUser,
                                "Error: ${e.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    Toast.makeText(
                        this,
                        "@string/badpassword",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    this,
                    "@string/emptyfields",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}