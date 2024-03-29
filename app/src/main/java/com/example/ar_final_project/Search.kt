package com.example.ar_final_project

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ar_final_project.databinding.FragmentSearchBinding
import com.example.ar_final_project.model.Product
import kotlinx.coroutines.launch

class Search : Fragment() {
    private lateinit var searchView: SearchView
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private val productList = mutableListOf<Product>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchView = view.findViewById(R.id.searchView)
        recyclerView = view.findViewById(R.id.recyclerView)

        // Set up your RecyclerView adapter and layout manager
        productAdapter = ProductAdapter(productList) { selectedProduct ->
            // Handle the item click here in the search method
            val intent = Intent(activity, ProductDetail::class.java)
            intent.putExtra("product",selectedProduct.id)
            startActivity(intent)
            // You can also launch your AR fragment or perform any other action
        }
        recyclerView.adapter = productAdapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrBlank()) {
                    searchItems(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrBlank()) {
                    searchItems(newText)
                }
                return true
            }
        })
        return view
    }

    private fun searchItems(query: String) {
        val service = ProductRetrofitService.ProductRetrofitServiceFactory.makeRetrofitService()
        lifecycleScope.launch {
            try {
                val response = service.searchItems(query)
                productList.clear()
                productList.addAll(response.products)
                productAdapter.notifyDataSetChanged()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}