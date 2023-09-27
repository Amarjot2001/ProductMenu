package com.example.productmenu.activities

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.productmenu.R
import com.example.productmenu.adapters.SettingsAdapter
import com.example.productmenu.databinding.ActivitySettingsBinding
import com.example.productmenu.model.SettingsModel
import com.google.android.material.navigation.NavigationBarView

class SettingsActivity : AppCompatActivity(), SettingsAdapter.OnItemClickListener {
    private var binding: ActivitySettingsBinding? = null

    private val changeActivity = NavigationBarView.OnItemSelectedListener { item ->
        when (item.itemId) {
            R.id.nav_settings -> {
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
            R.id.nav_home -> {
                val intent = Intent(this, ProductActivity::class.java)
                startActivity(intent)
                return@OnItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.toolbar?.setTitleTextColor(Color.BLACK)
        binding?.toolbar?.title = "Settings"
        setSupportActionBar(binding?.toolbar)

        binding?.bottomNavBar?.selectedItemId = R.id.nav_settings
        binding?.bottomNavBar?.setOnItemSelectedListener(changeActivity)

        val settingsItemList = ArrayList<SettingsModel>()
        settingsItemList.add(SettingsModel("Store Details"))
        settingsItemList.add(SettingsModel("Printer"))
        settingsItemList.add(SettingsModel("Others"))

        setUpRecyclerView(settingsItemList)
    }

    private fun setUpRecyclerView(list: ArrayList<SettingsModel>){
        val adapter = SettingsAdapter(list, this@SettingsActivity)
        binding?.rvSettingItems?.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.rvSettingItems?.adapter = adapter
    }

    override fun onItemClick(position: Int) {
        if(position == 0){
            val intent = Intent(this, StoreDetailActivity::class.java)
            startActivity(intent)
        }
        else{
            Toast.makeText(this, "Item on position $position Clicked", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}