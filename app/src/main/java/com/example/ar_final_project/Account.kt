package com.example.ar_final_project

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class Account : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_account)

        val userac = findViewById<EditText>(R.id.usernameac)
        val passac = findViewById<EditText>(R.id.userpassac)
        val confirpas = findViewById<EditText>(R.id.confirmuserpassac)
        val bgo = findViewById<AppCompatButton>(R.id.bgo)

        val registrationService = AccountRetrofitService
            .AccountRetrofitServiceFactory.makeRetrofitService()

        bgo.setOnClickListener {
            val usernameac = userac.text.toString()
            val passwac = passac.text.toString()
            val confirpasswac = confirpas.text.toString()

            if (usernameac.isEmpty() || confirpasswac.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            } else {
                if (passwac == confirpasswac) {
                    registerUser(registrationService, usernameac, passwac)
                } else {
                    Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun registerUser(
        registrationService: AccountRetrofitService,
        usernameac: String,
        passwac: String,
        ) {
        GlobalScope.launch(Dispatchers.Main) {
            try {
                val response = withContext(Dispatchers.IO) {
                    registrationService.register(usernameac, passwac)
                }
                if (response.str == "success") {
                    Toast.makeText(
                        this@Account,
                        "Usuario registrado exitosamente",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@Account,
                        "Error al registrar el usuario",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@Account,
                    "Error de conexión o del servidor",
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
        }
    }
}








