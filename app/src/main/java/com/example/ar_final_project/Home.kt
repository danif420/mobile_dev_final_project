import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ar_final_project.R
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

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
        Log.d("Home", "onCreateView")
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        recyclerView = view.findViewById(R.id.recyclerView) as RecyclerView

        // Load data from Firestore
        loadDataFromFirestore()
        return view
    }

    private fun loadDataFromFirestore() {
        val db = Firebase.firestore

        db.collection("products").get()
            .addOnSuccessListener { result ->
                products.clear()
                products.addAll(result.map { document ->
                    Home.Product(
                        document.getString("id") ?: "null",
                        document.getString("name") ?: "null",
                        document.getString("price") ?: "null"
                    )
                })

                // Update the RecyclerView with the new data
                updateRecyclerView()
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }

    private fun updateRecyclerView() {
        val adapter = ProductAdapter(products)
        recyclerView.adapter = adapter

        // Change the layout manager to GridLayoutManager
        val layoutManager = GridLayoutManager(requireContext(), 2) // 2 items per row
        recyclerView.layoutManager = layoutManager
    }

    data class Product(
        val id: String,
        val name: String,
        val price: String
    )
}

class ProductAdapter(private val productList: List<Home.Product>) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val productName: TextView = itemView.findViewById(R.id.productName)
        val productPrice: TextView = itemView.findViewById(R.id.productPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.product, parent, false)
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

