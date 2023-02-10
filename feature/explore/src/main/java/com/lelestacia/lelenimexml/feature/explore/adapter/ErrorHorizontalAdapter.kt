package com.lelestacia.lelenimexml.feature.explore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lelestacia.lelenimexml.feature.explore.databinding.HorizontalErrorAdapterBinding

class ErrorHorizontalAdapter(
    val message: String,
    val onRetryClicked: () -> Unit
) : RecyclerView.Adapter<ErrorHorizontalAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: HorizontalErrorAdapterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.tvErrorMessage.text = message
            binding.btnRetryConnection.setOnClickListener {
                onRetryClicked.invoke()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            HorizontalErrorAdapterBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind()
    }
}