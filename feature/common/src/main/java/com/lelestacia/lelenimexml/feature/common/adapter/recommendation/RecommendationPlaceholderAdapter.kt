package com.lelestacia.lelenimexml.feature.common.adapter.recommendation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lelestacia.lelenimexml.feature.common.databinding.RecommendationItemPlaceholderBinding

class RecommendationPlaceholderAdapter :
    RecyclerView.Adapter<RecommendationPlaceholderAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: RecommendationItemPlaceholderBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            RecommendationItemPlaceholderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
    }
}