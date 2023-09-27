package com.example.productmenu.model


class ListSingleton private constructor() {
    companion object {
        private var INSTANCE: ListSingleton? = null
        private lateinit var productList: ArrayList<ProductModel>
        fun getInstance(): ListSingleton {
            synchronized(this) {// only one thread may enter at a time
                if (INSTANCE == null) {
                    INSTANCE = ListSingleton()
                }
                return INSTANCE as ListSingleton
            }
        }
    }
    fun initializeList(){
        productList = ArrayList()
    }
    fun getItemList(): ArrayList<ProductModel> {
        return productList
    }

    fun addNewItem(product: ProductModel) {
        productList.add(product)
    }
}
