package com.example.productmenu.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.productmenu.model.ProductModel
import com.example.productmenu.databinding.CartItemBinding

class CartListAdapter(
    private var productList: ArrayList<ProductModel>, private var listener: OnProductClicked
    ) : RecyclerView.Adapter<CartListAdapter.MainViewHolder>() {
    interface OnProductClicked {
        fun onPlusClick(position: Int)
        fun onMinusClick(position: Int)
    }

    inner class MainViewHolder(
        private val itemBinding: CartItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bindItem(product: ProductModel) {
            itemBinding.imageView.setImageURI(product.uri)
            itemBinding.pName.text = product.name
            itemBinding.pPrice.text = product.price
            itemBinding.mPrice.text = product.price
            itemBinding.itemQuantity.text = product.quantity.toString()
            itemBinding.totalPrice.text = product.totalPrice.toString()
        }

        init {
            itemBinding.ivPlus.setOnClickListener {
                listener.onPlusClick(absoluteAdapterPosition)
            }
            itemBinding.ivMinus.setOnClickListener {
                listener.onMinusClick(absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            CartItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val product = productList[position]
        holder.bindItem(product)
    }

    override fun getItemCount(): Int {
        return productList.size
    }
}