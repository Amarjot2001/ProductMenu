package com.example.productmenu.databaseHandlers

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ProductEntity::class, StoreDetailEntity::class], version = 4)
abstract class ProductDatabase: RoomDatabase() {
    abstract fun productDao() : ProductDao

    companion object{// this allows us to add functions on the EmployeeDatabase class
    @Volatile // data changes on one thread made visible to other thread
    private var INSTANCE: ProductDatabase? = null

        //  singleton pattern is a design pattern that restricts the instantiation of a class to one object
//  Helper function to get the database
        fun getInstance(context: Context): ProductDatabase{
            synchronized(this) {// only one thread may enter at a time
                var instance = INSTANCE//for smart cast
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ProductDatabase::class.java,
                        "employee_database"
                    ).fallbackToDestructiveMigration()
                        .build()// wipes & rebuild if no migration object exists
                    INSTANCE = instance
                }
                return instance
            }
        }
    }

}