package com.lelestacia.lelenimexml.feature.common.adapter.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lelestacia.lelenimexml.feature.common.databinding.ItemListPlaceholderBinding

class ItemListPlaceholderAdapter : RecyclerView.Adapter<ItemListPlaceholderAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemListPlaceholderAdapter.ViewHolder {
        return ViewHolder(
            binding = ItemListPlaceholderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemListPlaceholderAdapter.ViewHolder, position: Int) {}

    override fun getItemCount(): Int = 15

    inner class ViewHolder(binding: ItemListPlaceholderBinding) :
        RecyclerView.ViewHolder(binding.root)
}