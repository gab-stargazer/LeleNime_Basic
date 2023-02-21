package com.lelestacia.lelenimexml.feature.explore.adapter.util

import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lelestacia.lelenimexml.feature.explore.databinding.FooterLoadstateBinding

class ExpandedFooterLoadStateAdapter(
    private val onRetryClicked: () -> Unit
) : LoadStateAdapter<ExpandedFooterLoadStateAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: FooterLoadstateBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(state: LoadState) {
            binding.btnRetryConnection.setOnClickListener { onRetryClicked.invoke() }
            when (state) {
                is LoadState.NotLoading -> {
                    TransitionManager.beginDelayedTransition(binding.root)
                    binding.cpLoading.visibility = View.GONE
                    binding.btnRetryConnection.visibility = View.GONE
                    binding.tvErrorMessage.visibility = View.GONE
                }

                LoadState.Loading -> {
                    TransitionManager.beginDelayedTransition(binding.root)
                    binding.btnRetryConnection.visibility = View.GONE
                    binding.tvErrorMessage.visibility = View.GONE
                    binding.cpLoading.visibility = View.VISIBLE
                }

                is LoadState.Error -> {
                    TransitionManager.beginDelayedTransition(binding.root)
                    binding.btnRetryConnection.visibility = View.VISIBLE
                    binding.tvErrorMessage.apply {
                        text = (loadState as LoadState.Error).error.message
                        visibility = View.GONE
                    }
                    binding.cpLoading.visibility = View.GONE
                }
            }
        }
    }

    override fun onBindViewHolder(
        holder: ExpandedFooterLoadStateAdapter.ViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): ExpandedFooterLoadStateAdapter.ViewHolder {
        return ViewHolder(
            binding = FooterLoadstateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}