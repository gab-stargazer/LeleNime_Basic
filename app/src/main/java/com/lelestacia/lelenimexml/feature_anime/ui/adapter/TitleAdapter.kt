package com.lelestacia.lelenimexml.feature_anime.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lelestacia.lelenimexml.R
import com.lelestacia.lelenimexml.core.domain.dto.animefull.Title
import com.lelestacia.lelenimexml.databinding.ItemListTitleBinding

class TitleAdapter : ListAdapter<Title, TitleAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemListTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    inner class ViewHolder(private val binding: ItemListTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Title) {
            binding.tvHolderTitle.text =
                binding.root.context.getString(R.string.placeholder_title, item.type)
            binding.tvValueTitle.text = item.title
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Title>() {
            override fun areItemsTheSame(oldItem: Title, newItem: Title): Boolean =
                oldItem.type == newItem.type

            override fun areContentsTheSame(oldItem: Title, newItem: Title): Boolean =
                oldItem == newItem
        }
    }
}