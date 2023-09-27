package com.example.productmenu.model

import android.net.Uri

data class ProductModel(
    var uri: Uri?,
    var name: String,
    var price: String,
    var description: String,
    var quantity: Int = 0,
    var totalPrice: Int = 0
)


