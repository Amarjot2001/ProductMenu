package com.example.productmenu.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.productmenu.adapters.CartListAdapter
import com.example.productmenu.model.ProductModel
import com.example.productmenu.R
import com.example.productmenu.databinding.ActivityProductCartBinding
import com.example.productmenu.model.ListSingleton
import com.google.android.material.navigation.NavigationBarView

class ProductCart : AppCompatActivity(), CartListAdapter.OnProductClicked {
    private var binding: ActivityProductCartBinding? = null
    private lateinit var adapter: CartListAdapter
    private var selectedItemsList = ArrayList<ProductModel>()
    private var productList = ArrayList<ProductModel>()
    private var adapterPosition: Int = 0

    private val changeActivity = NavigationBarView.OnItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_order -> {
                return@OnItemSelectedListener false
            }
            R.id.nav_add -> {
                val intent = Intent(this, ProductAdd::class.java)
                startActivity(intent)
                return@OnItemSelectedListener true
            }
            R.id.nav_home -> {
                val intent = Intent(this, ProductActivity::class.java)
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
        binding = ActivityProductCartBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.toolbar?.setTitleTextColor(Color.BLACK)
        binding?.toolbar?.title = "Cart"
        setSupportActionBar(binding?.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(onBackPressedCallback)
        binding?.toolbar?.setNavigationOnClickListener {
            finish()
        }

        productList = ListSingleton.getInstance().getItemList()
        adapterPosition = intent.getIntExtra("adapterPosition", 0)
        addSelectedProducts()

        binding?.checkOutBtn?.setOnClickListener {
            resetList()
            val intent = Intent(this, ProductActivity::class.java)
            startActivity(intent)
        }
        binding?.bottomNavBar?.selectedItemId = R.id.nav_order
        binding?.bottomNavBar?.setOnItemSelectedListener(changeActivity)
    }

    private fun addSelectedProducts() {
        for (product in productList) {
            if (product.quantity > 0) {
                selectedItemsList.add(product)
            }
        }
        if (selectedItemsList.isNotEmpty()) {
            adapter = CartListAdapter(selectedItemsList, this)
            binding?.rvCartProducts?.visibility = View.VISIBLE
            binding?.noItems?.visibility = View.GONE
            binding?.rvCartProducts?.layoutManager = LinearLayoutManager(this)
            binding?.rvCartProducts?.adapter = adapter
        } else {
            binding?.rvCartProducts?.visibility = View.GONE
            binding?.noItems?.visibility = View.VISIBLE
        }
    }

    private fun resetList() {
        for (product in productList) {
            if (product.quantity > 0) {
                product.quantity = 0
                product.totalPrice = 0
                Log.i("Reset products", "$product")
            }
        }
    }

    private fun updateTotalPrice() {
        val item = selectedItemsList[adapterPosition]
        val price: Int = Integer.parseInt(item.price)
        val totalPrice = item.quantity * price
        item.totalPrice = totalPrice
    }

    override fun onPlusClick(position: Int) {
        val item = selectedItemsList[position]
        item.quantity += 1
        updateTotalPrice()
        adapter.notifyDataSetChanged()
    }

    override fun onMinusClick(position: Int) {
        adapterPosition = position
        val item = selectedItemsList[position]
        if (item.quantity > 1) {
            item.quantity -= 1
            updateTotalPrice()
        }
        adapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}