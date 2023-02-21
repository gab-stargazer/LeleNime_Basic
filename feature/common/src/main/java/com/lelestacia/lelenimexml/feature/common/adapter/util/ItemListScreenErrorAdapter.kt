package com.lelestacia.lelenimexml.feature.common.adapter.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lelestacia.lelenimexml.feature.common.databinding.ItemListScreenErrorBinding

class ItemListScreenErrorAdapter(
    val errorMessage: String,
    val onRetry: () -> Unit
) : RecyclerView.Adapter<ItemListScreenErrorAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemListScreenErrorAdapter.ViewHolder {
        return ViewHolder(
            binding = ItemListScreenErrorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemListScreenErrorAdapter.ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = 1

    inner class ViewHolder(private val binding: ItemListScreenErrorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.tvErrorMessage.text = errorMessage
            binding.btnRetryConnection.setOnClickListener {
                onRetry.invoke()
            }
        }
    }
}