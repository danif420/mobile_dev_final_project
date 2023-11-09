package com.example.ar_final_project

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val login=findViewById<TextView>(R.id.login)
        val about=findViewById<TextView>(R.id.about)

        val db = Firebase.firestore

        db.collection("products")
            .get()
            .addOnSuccessListener { result ->
                val products = result.map { document ->
                        Product(
                            document.getString("id") ?: "null",
                            document.getString("name") ?: "null",
                            document.getString("price") ?: "null"
                        )

                }
                val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
                val adapter = ProductAdapter(products) // Pass the products list to the adapter
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }


    }

}

data class Product(
    val id: String,
    val name: String,
    val price: String
)

class ProductAdapter(private val productList: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentProduct = productList[position]
        holder.productName.text = currentProduct.name
        holder.productPrice.text = currentProduct.price
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}