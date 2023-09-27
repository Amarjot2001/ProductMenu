package com.example.productmenu.databaseHandlers

import android.app.Application

    class ProductApplication: Application() {
        val db by lazy {
            ProductDatabase.getInstance(this)
        }
    }

