package com.example.productmenu.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.productmenu.databinding.SettingItemBinding
import com.example.productmenu.model.SettingsModel

class SettingsAdapter(private val settingItemList: ArrayList<SettingsModel>, private var listener : OnItemClickListener
): RecyclerView.Adapter<SettingsAdapter.MainViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    inner class MainViewHolder(itemBinding: SettingItemBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        var storeName = itemBinding.tvStoreName

        init {
            itemBinding.root.setOnClickListener {
                listener.onItemClick(absoluteAdapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            SettingItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val settingItem = settingItemList[position]
        holder.storeName.text = settingItem.mStoreName
    }

    override fun getItemCount(): Int {
        return settingItemList.size
    }
}