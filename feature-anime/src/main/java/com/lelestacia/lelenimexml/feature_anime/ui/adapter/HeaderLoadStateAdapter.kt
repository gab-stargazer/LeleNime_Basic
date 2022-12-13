package com.lelestacia.lelenimexml.feature_anime.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lelestacia.lelenimexml.feature_anime.databinding.LoadStateHeaderBinding

class HeaderLoadStateAdapter(val onRetry: () -> Unit) :
    LoadStateAdapter<HeaderLoadStateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val binding =
            LoadStateHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class ViewHolder(private val binding: LoadStateHeaderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {
            binding.btnRetry.setOnClickListener {
                onRetry.invoke()
            }
            when (loadState) {
                is LoadState.NotLoading ->
                    binding.apply {
                        btnRetry.visibility = View.GONE
                        tvLoading.visibility = View.GONE
                        progressCircular.visibility = View.GONE
                    }
                LoadState.Loading ->
                    binding.apply {
                        btnRetry.visibility = View.GONE
                        tvLoading.visibility = View.VISIBLE
                        progressCircular.visibility = View.VISIBLE
                    }
                is LoadState.Error ->
                    binding.apply {
                        btnRetry.visibility = View.VISIBLE
                        tvLoading.visibility = View.GONE
                        progressCircular.visibility = View.GONE
                    }
            }
        }
    }
}