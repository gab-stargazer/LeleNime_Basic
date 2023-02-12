package com.lelestacia.lelenimexml.feature.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lelestacia.lelenimexml.core.model.recommendation.Recommendation
import com.lelestacia.lelenimexml.feature.common.databinding.RecommendationItemBinding
import com.lelestacia.lelenimexml.core.common.util.DateParser

class RecommendationPagingDataAdapter :
    PagingDataAdapter<Recommendation, RecommendationPagingDataAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: RecommendationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Recommendation) {
            binding.apply {
                tvUsername.text = item.userName
                tvDate.text = DateParser().invoke(item.date)
                tvContent.text = item.content

                val mLayoutManager = object : LinearLayoutManager(
                    root.context,
                    RecyclerView.HORIZONTAL,
                    false
                ) {
                    override fun canScrollHorizontally(): Boolean = false
                }

                val genericModelListAdapter = GenericModelListAdapter()

                rvGenericModel.apply {
                    adapter = genericModelListAdapter
                    layoutManager = mLayoutManager
                    setHasFixedSize(true)
                }
                genericModelListAdapter.submitList(item.entry)
            }
        }
    }

    override fun onBindViewHolder(
        holder: RecommendationPagingDataAdapter.ViewHolder,
        position: Int
    ) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendationPagingDataAdapter.ViewHolder {
        return ViewHolder(
            RecommendationItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Recommendation>() {
            override fun areItemsTheSame(
                oldItem: Recommendation,
                newItem: Recommendation
            ): Boolean =
                oldItem.malID == newItem.malID

            override fun areContentsTheSame(
                oldItem: Recommendation,
                newItem: Recommendation
            ): Boolean =
                oldItem == newItem
        }
    }
}