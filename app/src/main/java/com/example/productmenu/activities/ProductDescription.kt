package com.example.productmenu.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import com.example.productmenu.model.ProductModel
import com.example.productmenu.R
import com.example.productmenu.databinding.ActivityProductDescriptionBinding
import com.example.productmenu.model.ListSingleton

class ProductDescription : AppCompatActivity() {

    private var binding: ActivityProductDescriptionBinding? = null
    private var quantity: Int = 0
    private var totalPrice: Int = 0
    private var adapterPosition: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDescriptionBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(binding?.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
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

        adapterPosition = intent.getIntExtra("position", 0)
        val productList = ListSingleton.getInstance().getItemList()
        setItems(productList)

        binding?.ivPlus?.setOnClickListener {
            quantity += 1
            binding?.totalItems?.text = quantity.toString()
            showPrice()
        }
        binding?.ivMinus?.setOnClickListener {
            if (quantity > 1) {
                quantity -= 1
            }
            binding?.totalItems?.text = quantity.toString()
            showPrice()
        }
        binding?.addBtn?.setOnClickListener {
            updateSelectedProduct()
            val intent = Intent(this, ProductCart::class.java)
            intent.putExtra("adapterPosition", adapterPosition)
            finish()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.cart, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.cart1 -> {
                val intent = Intent(this, ProductCart::class.java)
                intent.putExtra("adapterPosition", adapterPosition)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setItems(productList: ArrayList<ProductModel>) {
        val items = productList[adapterPosition]
        binding?.imageView?.setImageURI(items.uri)
        binding?.tvProductName?.text = items.name
        binding?.tvProductPrice?.text = items.price
        binding?.tvDetail?.text = items.description
    }

    private fun showPrice() {
        val price: Int = Integer.parseInt(binding?.tvProductPrice?.text as String)
        totalPrice = quantity * price
        binding?.tvPrice?.text = totalPrice.toString()
    }

    private fun updateSelectedProduct() {
        val productList = ListSingleton.getInstance().getItemList()
        val items = productList[adapterPosition]
        items.quantity = quantity // update the value of quantity at a index
        items.totalPrice = totalPrice
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}