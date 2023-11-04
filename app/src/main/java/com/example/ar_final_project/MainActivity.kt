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
        val products=listOf(
            hashMapOf(
                "id" to "001",
                "name" to "small cake",
                "price" to "$ 20.000"
            ),
            hashMapOf(
                "id" to "002",
                "name" to "medium cake",
                "price" to "$ 32.000"
            ),
            hashMapOf(
                "id" to "003",
                "name" to "coffee",
                "price" to "$ 2.000"
            ),
            hashMapOf(
                "id" to "004",
                "name" to "hamburger",
                "price" to "$ 18.000"
            )
        )
        for (x in products) {
            db.collection("products")
                .add(x)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error adding document", e)
                }

            login.setOnClickListener {
                val intent= Intent(this,Login::class.java)
                startActivity(intent)
            }
            about.setOnClickListener {
                val intent= Intent(this,About::class.java)
                startActivity(intent)
            }

        }

        db.collection("products")
            .get()
            .addOnSuccessListener { result ->
                val products = result.map { document ->
                    Product(
                        document.id.toInt(),
                        document.getString("name")!!,
                        document.getLong("price")!!.toInt()
                    )
                }
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting documents.", exception)
            }
    }

}

data class Product(
    val id: Int,
    val name: String,
    val price: Int
)

class ProductAdapter(private val products: List<Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {
    class
    ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)


        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
    }

    override

    fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }
    override
    fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.nameTextView.text = product.name
        holder.priceTextView.text = product.price.toString()
    }
    override fun getItemCount(): Int {
        return products.size
    }
}

