package com.lelestacia.lelenimexml.feature.explore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.lelestacia.lelenimexml.feature.explore.databinding.ItemCardAnimeHorizontalBinding

class PlaceHolderHorizontalAdapter(private val placeHolderCount: List<Int>) :
    RecyclerView.Adapter<PlaceHolderHorizontalAdapter.ViewHolder>() {

    inner class ViewHolder(
        binding: ItemCardAnimeHorizontalBinding
    ) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCardAnimeHorizontalBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {}

    override fun getItemCount(): Int = placeHolderCount.size
}