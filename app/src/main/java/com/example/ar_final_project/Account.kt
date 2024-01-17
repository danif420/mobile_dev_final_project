package com.example.ar_final_project

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ar_final_project.model.Product
import kotlinx.coroutines.launch

class Account : Fragment() {
    var username:String?=""
    var token:String?=""
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private val productList = mutableListOf<Product>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_account, container, false)

        username = arguments?.getString("username")
        token = arguments?.getString("token")
        println(token)
        var textusername = view.findViewById<TextView>(R.id.username)
        textusername?.text = username


        val logout = view.findViewById<AppCompatButton>(R.id.logout)
        val delete = view.findViewById<AppCompatButton>(R.id.delete)

        val service = ProductRetrofitService.ProductRetrofitServiceFactory.makeRetrofitService()
        lifecycleScope.launch {
            try {
                val response = service.myProducts(getUserId(username).id.toString())
                productList.addAll(response.products)
                println(productList.toString())
                recyclerView = view.findViewById(R.id.recyclerView)
                productAdapter = ProductAdapter(productList) { selectedProduct ->
                    // Handle the item click here in the search method
                    val intent = Intent(activity, ProductDetail::class.java)
                    intent.putExtra("product",selectedProduct.id)
                    startActivity(intent)
                }
                recyclerView.adapter = productAdapter
                recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        logout.setOnClickListener {
            val service = RetrofitService.RetrofitServiceFactory.makeRetrofitService()
            lifecycleScope.launch {
                try {
                    service.logout("Token $token")
                    val intent = Intent(context, Login::class.java)
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        delete.setOnClickListener {
            val service = RetrofitService.RetrofitServiceFactory.makeRetrofitService()
            lifecycleScope.launch {
                try {
                    service.deleteUser(getUserId(username).id.toString())
                    val intent = Intent(context, Login::class.java)
                    startActivity(intent)
                } catch (e: Exception) {
                    Toast.makeText(
                        context,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        return view
    }
    private suspend fun getUserId(username:String?): ResponseId {
        val service = ProductRetrofitService.ProductRetrofitServiceFactory.makeRetrofitService()
        return service.userId(username)
    }
}