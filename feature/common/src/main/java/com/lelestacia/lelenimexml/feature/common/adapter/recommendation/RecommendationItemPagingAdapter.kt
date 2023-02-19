package com.lelestacia.lelenimexml.feature.common.adapter.recommendation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lelestacia.lelenimexml.core.common.util.DateParser
import com.lelestacia.lelenimexml.core.model.recommendation.Recommendation
import com.lelestacia.lelenimexml.feature.common.adapter.GenericModelListAdapter
import com.lelestacia.lelenimexml.feature.common.databinding.RecommendationItemBinding

class RecommendationItemPagingAdapter(
    private val onItemClicked: (Recommendation) -> Unit,
    private val onNextButtonClicked: () -> Unit
) :
    PagingDataAdapter<Recommendation, RecommendationItemPagingAdapter.ViewHolder>(DIFF_CALLBACK) {

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

                root.setOnClickListener { onItemClicked(item) }
                btnNextRecommendation.setOnClickListener { onNextButtonClicked.invoke() }
            }
        }
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
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