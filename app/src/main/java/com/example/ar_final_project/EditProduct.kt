package com.example.ar_final_project

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody

class EditProduct : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_product)
        val productId=intent.getIntExtra("productId", -1)
        val pname = intent.getStringExtra("name")
        val pprice = intent.getStringExtra("price")
        val pquant = intent.getStringExtra("quantity")
        val save = findViewById<AppCompatButton>(R.id.save)
        val cancel = findViewById<AppCompatButton>(R.id.cancel)
        val delete = findViewById<AppCompatButton>(R.id.delete)
        var name = findViewById<EditText>(R.id.name)
        var price = findViewById<EditText>(R.id.price)
        var quant = findViewById<EditText>(R.id.quant)
        var img = findViewById<ImageView>(R.id.img)

        name.setText(pname)
        price.setText(pprice)
        quant.setText(pquant)

        val productService = ProductRetrofitService.ProductRetrofitServiceFactory.makeRetrofitService()

        // Use lifecycleScope to launch a coroutine
        lifecycleScope.launch {
            try {
                val response = productService.getProduct(productId.toString())
                Picasso.get()
                    .load(response.img)
                    .into(img)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        save.setOnClickListener {
            lifecycleScope.launch {
                val service = UploadRetrofitService.makeRetrofitService()
                service.editProduct(
                    productId.toString(),
                    name.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    price.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                    quant.text.toString().toRequestBody("text/plain".toMediaTypeOrNull()),
                )
                finish()
            }
        }
        cancel.setOnClickListener {finish()}
        delete.setOnClickListener {
            lifecycleScope.launch {
                val productService = ProductRetrofitService.ProductRetrofitServiceFactory.makeRetrofitService()
                productService.deleteProduct(productId.toString())
                finish()
            }

        }
    }
    override fun onBackPressed() {
        finish() // Close ARActivity and go back to MainActivity
    }
}