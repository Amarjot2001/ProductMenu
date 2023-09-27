package com.example.productmenu.activities

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.productmenu.R
import com.example.productmenu.adapters.ProductAdapter
import com.example.productmenu.databaseHandlers.ProductApplication
import com.example.productmenu.databaseHandlers.ProductEntity
import com.example.productmenu.databinding.ActivityMenuAppBinding
import com.example.productmenu.model.ListSingleton
import com.example.productmenu.model.ProductModel
import com.google.android.material.navigation.NavigationBarView
import kotlinx.coroutines.launch


class ProductActivity : AppCompatActivity(), ProductAdapter.OnItemClickListener {
    private var binding: ActivityMenuAppBinding? = null
    private var adapter: ProductAdapter? = null
    private lateinit var productList: ArrayList<ProductModel>

    private val changeActivity = NavigationBarView.OnItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_home -> {
                return@OnItemSelectedListener false
            }
            R.id.nav_add -> {
                val intent = Intent(this, ProductAdd::class.java)
                startActivity(intent)
                return@OnItemSelectedListener true
            }
            R.id.nav_order -> {
                val intent = Intent(this, ProductCart::class.java)
                startActivity(intent)
                return@OnItemSelectedListener true
            }
            R.id.nav_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                return@OnItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMenuAppBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.toolbar?.setTitleTextColor(Color.BLACK)
        setSupportActionBar(binding?.toolbar)// set actionbar on the current activity

        ListSingleton.getInstance().initializeList()
        productList = ArrayList()
        val productDao = (application as ProductApplication).db.productDao()
        lifecycleScope.launch {
            productDao.fetchAllProducts().collect {
                val productListDb = ArrayList(it)
                Log.i("exact product size", "${productListDb.size}")
                addRecordToModel(productListDb)
                productList = ListSingleton.getInstance().getItemList()
                runOnUiThread {
                    setupListOfDataIntoRecyclerView(productList)
                }
                binding?.rvProducts?.post{
                    adapter?.notifyDataSetChanged()
                }
            }
        }
        binding?.bottomNavBar?.selectedItemId = R.id.nav_home
        binding?.bottomNavBar?.setOnItemSelectedListener(changeActivity)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cart_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add -> {
                val intent = Intent(this, ProductAdd::class.java)
                startActivity(intent)
            }
            R.id.cart -> {
                val intent = Intent(this, ProductCart::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onItemClick(position: Int) {
        val intent = Intent(this, ProductDescription::class.java)
        intent.putExtra("position", position)
        startActivity(intent)
    }

    private fun setupListOfDataIntoRecyclerView(productList: ArrayList<ProductModel>) {
        if (productList.isNotEmpty()) {
            adapter = ProductAdapter(productList, this, this)

            binding?.rvProducts?.layoutManager = GridLayoutManager(applicationContext, 2)
            binding?.rvProducts?.adapter = adapter
            binding?.rvProducts?.visibility = View.VISIBLE
            binding?.tvNoProductsAdded?.visibility = View.INVISIBLE

        } else {
            binding?.rvProducts?.visibility = View.INVISIBLE
            binding?.tvNoProductsAdded?.visibility = View.VISIBLE
        }
    }

    private fun addRecordToModel(productList: ArrayList<ProductEntity>) {
        for (product in productList) {
            val image = product.image
            val name = product.name
            val price = product.price
            val desc = product.description
            ListSingleton.getInstance().addNewItem(ProductModel(image.toUri(), name, price, desc))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}