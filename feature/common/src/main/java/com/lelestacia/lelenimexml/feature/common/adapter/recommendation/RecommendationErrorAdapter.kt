package com.lelestacia.lelenimexml.feature.common.adapter.recommendation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lelestacia.lelenimexml.feature.common.databinding.RecommendationItemErrorBinding

class RecommendationErrorAdapter(
    private val message: String,
    private val onRetry: () -> Unit
) : RecyclerView.Adapter<RecommendationErrorAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: RecommendationItemErrorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.tvErrorMessage.text = message
            binding.btnRetryConnection.setOnClickListener {
                onRetry.invoke()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            RecommendationItemErrorBinding.inflate(
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