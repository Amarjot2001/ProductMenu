package com.example.productmenu.adapters


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.productmenu.model.ProductModel
import com.example.productmenu.databinding.MenuProductBinding

class ProductAdapter(
    private var productList: ArrayList<ProductModel>,
    val context: Context,
    private var listener: OnItemClickListener
) : RecyclerView.Adapter<ProductAdapter.MainViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class MainViewHolder(
        private val itemBinding: MenuProductBinding) : RecyclerView.ViewHolder(itemBinding.root) {

        var imageView = itemBinding.pImage
        var name = itemBinding.pName
        var price = itemBinding.pPrice


        init {
            itemBinding.root.setOnClickListener {
                listener.onItemClick(absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            MenuProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val product = productList[position]
        holder.imageView.setImageURI(product.uri)
        holder.name.text = product.name
        holder.price.text = product.price
    }

    override fun getItemCount(): Int {
        return productList.size
    }


}