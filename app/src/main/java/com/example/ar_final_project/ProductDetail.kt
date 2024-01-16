package com.example.ar_final_project

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.lifecycleScope
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class ProductDetail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)
        val productId=intent.getIntExtra("product", -1)
        val bar = findViewById<AppCompatButton>(R.id.bar)
        val bbuy = findViewById<AppCompatButton>(R.id.bbuy)
        var name = findViewById<TextView>(R.id.name)
        var price = findViewById<TextView>(R.id.price)
        var quant = findViewById<TextView>(R.id.quant)
        var img = findViewById<ImageView>(R.id.img)

        val productService = ProductRetrofitService.ProductRetrofitServiceFactory.makeRetrofitService()

        // Use lifecycleScope to launch a coroutine
        lifecycleScope.launch {
            try {
                val response = productService.getProduct(productId.toString())
                name.text = response.name
                price.text = response.price
                quant.text = response.quantity.toString()
                Picasso.get()
                    .load(response.img)
                    .into(img)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        bar.setOnClickListener {
            val intent = Intent(this, AR::class.java)
            intent.putExtra("productId",productId)
            startActivity(intent)
        }
        bbuy.setOnClickListener {
            lifecycleScope.launch {
                try {
                    val response = productService.buyProduct(productId.toString())
                    quant.text = response.quantity.toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    override fun onBackPressed() {
        finish() // Close ARActivity and go back to MainActivity
    }
}