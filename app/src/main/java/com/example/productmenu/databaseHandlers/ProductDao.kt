package com.example.productmenu.databaseHandlers

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ProductDao {
        @Insert
        suspend fun insert(productEntity: ProductEntity)
        @Update
        suspend fun update(productEntity: ProductEntity)
//        @Delete
//        suspend fun delete(productEntity: ProductEntity)
        @Query("SELECT * FROM `product-table`")
        fun fetchAllProducts(): Flow<List<ProductEntity>>
//        @Query("SELECT * FROM `product-table` where id = :id")
//        fun fetchProductsById(id: Int): Flow<ProductEntity>

        @Insert
        suspend fun insert(storeDetailEntity: StoreDetailEntity)
        @Query("SELECT * FROM `storeDetail-table`")
        fun fetchAllStoreDetail(): Flow<List<StoreDetailEntity>>
    }