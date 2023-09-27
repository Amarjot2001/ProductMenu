package com.example.productmenu.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.productmenu.databaseHandlers.ProductApplication
import com.example.productmenu.databaseHandlers.ProductDao
import com.example.productmenu.databaseHandlers.StoreDetailEntity
import com.example.productmenu.databinding.ActivityStoreDetailBinding
import kotlinx.coroutines.launch

class StoreDetailActivity : AppCompatActivity() {
    private var binding: ActivityStoreDetailBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreDetailBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val productDao = (application as ProductApplication).db.productDao()
        lifecycleScope.launch {
            productDao.fetchAllStoreDetail().collect {
                val storeDetailDb = ArrayList(it)
                Log.i("store detail size", "${storeDetailDb.size}")
                Log.i("store detail data", storeDetailDb.toString())
            }
        }

        binding?.saveDetailBtn?.setOnClickListener {
            val dao = (application as ProductApplication).db.productDao()
            addStoreDetailToDatabase(dao)
        }
    }

    private fun addStoreDetailToDatabase(productDao: ProductDao) {
        when {
            binding?.etStoreName?.text.isNullOrEmpty() -> {
                Toast.makeText(this, "Please enter store name", Toast.LENGTH_LONG).show()
            }
            binding?.etAddress?.text.isNullOrEmpty() -> {
                Toast.makeText(this, "Please enter Address", Toast.LENGTH_LONG).show()
            }

            else -> {
                val name = binding?.etStoreName?.text.toString()
                val address = binding?.etAddress?.text.toString()
                lifecycleScope.launch {
                    productDao.insert(StoreDetailEntity(0, name, address))
                }
                Toast.makeText(applicationContext, "Record saved", Toast.LENGTH_LONG).show()
                binding?.etStoreName?.text?.clear()
                binding?.etAddress?.text?.clear()
            }
        }
    }

}