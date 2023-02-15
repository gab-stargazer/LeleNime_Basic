package com.lelestacia.lelenimexml.feature.common.adapter.util

import android.transition.TransitionManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lelestacia.lelenimexml.feature.common.databinding.HorizontalFooterLoadstateBinding

class HorizontalLoadStateAdapter(
    private val onRetryClicked: () -> Unit
) : LoadStateAdapter<HorizontalLoadStateAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: HorizontalFooterLoadstateBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(loadState: LoadState) {
            when (loadState) {
                is LoadState.NotLoading -> Unit
                LoadState.Loading ->
                    binding.apply {
                        TransitionManager.beginDelayedTransition(root)
                        //Hide Error State
                        tvErrorMessage.visibility = View.GONE
                        btnRetry.visibility = View.GONE
                        //Display Loading State
                        tvLoadingMessage.visibility = View.VISIBLE
                        progressCircular.visibility = View.VISIBLE
                    }
                is LoadState.Error ->
                    binding.apply {
                        TransitionManager.beginDelayedTransition(binding.root)
                        //Set Error Message
                        tvErrorMessage.text = loadState.error.message
                        //Hide Loading State
                        tvLoadingMessage.visibility = View.GONE
                        progressCircular.visibility = View.GONE
                        //Display Error State
                        tvErrorMessage.visibility = View.VISIBLE
                        btnRetry.visibility = View.VISIBLE
                    }
            }
            binding.btnRetry.setOnClickListener {
                onRetryClicked.invoke()
            }
        }
    }

    override fun onBindViewHolder(
        holder: HorizontalLoadStateAdapter.ViewHolder,
        loadState: LoadState
    ) {
        holder.bind(loadState = loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): HorizontalLoadStateAdapter.ViewHolder {
        return ViewHolder(
            binding = HorizontalFooterLoadstateBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }
}