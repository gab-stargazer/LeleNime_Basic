package com.lelestacia.lelenimexml.feature.common.adapter.util

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lelestacia.lelenimexml.feature.common.databinding.HorizontalErrorBinding

class HorizontalErrorAdapter(
    val message: String,
    val onRetryClicked: () -> Unit
) : RecyclerView.Adapter<HorizontalErrorAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: HorizontalErrorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.tvErrorMessage.text = message
            binding.btnRetryConnection.setOnClickListener {
                onRetryClicked.invoke()
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            binding = HorizontalErrorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = 1
}