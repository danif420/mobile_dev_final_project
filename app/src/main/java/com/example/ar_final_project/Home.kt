package com.example.ar_final_project

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ar_final_project.model.Product
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class Home : Fragment() {

    private val products = mutableListOf<Product>()
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("com.example.ar_final_project.Home", "onCreateView")
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val productService = ProductRetrofitService.ProductRetrofitServiceFactory.makeRetrofitService()

        // Use lifecycleScope to launch a coroutine
        lifecycleScope.launch {
            try {
                // Make the API call
                val response = productService.products()
                // Check if the response is successful and not empty
                val productsFromServer = response.products
                updateRecyclerView(productsFromServer)

            } catch (e: Exception) {
                // Handle exceptions, such as network errors
                Toast.makeText(requireContext(), "Failed to get products from the server", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
                Toast.makeText(requireContext(), "Network error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateRecyclerView(products: List<Product>) {
        val adapter = ProductAdapter(products) { selectedProduct ->
            val intent = Intent(activity, ProductDetail::class.java)
            intent.putExtra("product",selectedProduct.id)
            startActivity(intent)
        }

        recyclerView.adapter = adapter

        // Change the layout manager to GridLayoutManager
        val layoutManager = GridLayoutManager(requireContext(), 2) // 2 items per row
        recyclerView.layoutManager = layoutManager
    }
}

class ProductAdapter(private val productList: List<Product>, private var onItemClick: (Product) -> Unit) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
        val productImage: ImageView = itemView.findViewById(R.id.productImage)
        // click listener
        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(productList[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentProduct = productList[position]
        holder.productName.id = currentProduct.id
        holder.productName.text = currentProduct.name
        holder.productPrice.text = currentProduct.price
        Picasso.get()
            .load(currentProduct.img)
            .into(holder.productImage)
    }

    override fun getItemCount(): Int {
        return productList.size
    }
    // set the click listener from the fragment
    fun setOnItemClickListener(listener: (Product) -> Unit) {
        onItemClick = listener
    }
}