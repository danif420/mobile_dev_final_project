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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth

class Login : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val home=findViewById<TextView>(R.id.home)
        val about=findViewById<TextView>(R.id.about)
        val blogin=findViewById<AppCompatButton>(R.id.blogin)
        val uname=findViewById<EditText>(R.id.username)
        val upass=findViewById<EditText>(R.id.userpass)
        auth = Firebase.auth
        blogin.setOnClickListener(){
            var name=uname.text.toString()
            var pass=upass.text.toString()
            if (name.isNotEmpty() && pass.isNotEmpty()) {
                auth.signInWithEmailAndPassword(name, pass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val text = "Bienvenido profe Franklin!"
                            val duration = Toast.LENGTH_SHORT
                            val toast = Toast.makeText(this, text, duration)
                            toast.show()
                            val intent= Intent(this,MainActivity::class.java)
                            startActivity(intent)
                            val user: FirebaseUser? = auth.currentUser
                        } else {
                            val text = "Error en inicio de sesión"
                            val duration = Toast.LENGTH_SHORT
                            val toast = Toast.makeText(this, text, duration)
                            toast.show()
                        }
                    }
            }
        }
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