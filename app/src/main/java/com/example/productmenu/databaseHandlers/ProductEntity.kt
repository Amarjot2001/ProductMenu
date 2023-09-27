package com.example.productmenu.databaseHandlers

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "product-table")
data class ProductEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String = "",
    @ColumnInfo(name = "price")
    val price: String = "",
    @ColumnInfo(name = "description")
    val description: String = "",
    @ColumnInfo(name = "image")
    val image: String = ""
)

@Entity(tableName = "storeDetail-table")
data class StoreDetailEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "store-name")
    val sName: String = "",
    @ColumnInfo(name = "location")
    val sLocation: String = ""
)
