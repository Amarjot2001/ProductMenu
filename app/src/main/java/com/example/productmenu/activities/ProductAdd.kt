package com.example.productmenu.activities

import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.ImageDecoder
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.lifecycleScope
import com.example.productmenu.model.ProductModel
import com.example.productmenu.R
import com.example.productmenu.databaseHandlers.ProductApplication
import com.example.productmenu.databaseHandlers.ProductDao
import com.example.productmenu.databaseHandlers.ProductEntity
import com.example.productmenu.databinding.ActivityProductaddBinding
import com.google.android.material.navigation.NavigationBarView
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.util.*
import kotlin.collections.ArrayList

class ProductAdd : AppCompatActivity() {

    private var binding: ActivityProductaddBinding? = null
    private var imageUri: Uri? = null
    private var saveImageTOInternalStorage: Uri? = null
    private lateinit var productList: ArrayList<ProductModel>

    private val changeActivity = NavigationBarView.OnItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_add -> {
                return@OnItemSelectedListener false
            }
            R.id.nav_home -> {
                val intent = Intent(this, ProductActivity::class.java)
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
        binding = ActivityProductaddBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.toolbar?.setTitleTextColor(Color.BLACK)
        binding?.toolbar?.title = "Add Product"
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

        productList = ArrayList()

        binding?.editImage?.setOnClickListener {
            getImage.launch("image/*")
        }
        binding?.addBtn?.setOnClickListener {
            val dao = (application as ProductApplication).db.productDao()
            addRecordToDatabase(dao)
            Toast.makeText(applicationContext, "Button Clicked", Toast.LENGTH_LONG).show()
            finish()
        }
        binding?.bottomNavBar?.selectedItemId = R.id.nav_add
        binding?.bottomNavBar?.setOnItemSelectedListener(changeActivity)
    }

    private val getImage = registerForActivityResult(
        ActivityResultContracts.GetContent(),
        ActivityResultCallback {
            imageUri = it
            try {
                val source = ImageDecoder.createSource(contentResolver, imageUri!!)
                val bitmap = ImageDecoder.decodeBitmap(source)
                saveImageTOInternalStorage = saveImageIntoDeviceStorage(bitmap)
                Log.i("saved Image", "Path::$saveImageTOInternalStorage")
                binding?.image?.setImageURI(saveImageTOInternalStorage)
            } catch (e: IOException) {
                e.printStackTrace()
                Toast.makeText(this, "Failed to load image from Gallery", Toast.LENGTH_LONG)
                    .show()
            }
        }
    )

    private fun addRecordToDatabase(productDao: ProductDao) {
        when {
            binding?.etProductName?.text.isNullOrEmpty() -> {
                Toast.makeText(this, "Please enter Name", Toast.LENGTH_LONG).show()
            }
            binding?.etPrice?.text.isNullOrEmpty() -> {
                Toast.makeText(this, "Please enter price", Toast.LENGTH_LONG).show()
            }
            binding?.etDesc?.text.isNullOrEmpty() -> {
                Toast.makeText(this, "Please enter description", Toast.LENGTH_LONG).show()
            }
            saveImageTOInternalStorage == null -> {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_LONG).show()
            }
            else -> {
                val name = binding?.etProductName?.text.toString()
                val price = binding?.etPrice?.text.toString()
                val description = binding?.etDesc?.text.toString()
                lifecycleScope.launch {
                    productDao.insert(
                        ProductEntity(
                            0,
                            name,
                            price,
                            description,
                            saveImageTOInternalStorage.toString()
                        )
                    )
                }
                binding?.etProductName?.text?.clear()
                binding?.etPrice?.text?.clear()
                binding?.etDesc?.text?.clear()
            }
        }
    }

    private fun saveImageIntoDeviceStorage(bitmap: Bitmap): Uri {
        val wrapper = ContextWrapper(applicationContext)
        var file = wrapper.getDir("PRODUCT IMAGES", ContextWrapper.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")
        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Uri.parse(file.absolutePath)
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}