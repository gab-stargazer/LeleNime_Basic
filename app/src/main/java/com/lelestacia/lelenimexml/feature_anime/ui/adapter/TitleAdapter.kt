package com.lelestacia.lelenimexml.feature_anime.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lelestacia.lelenimexml.R
import com.lelestacia.lelenimexml.databinding.ItemListTitleBinding

class TitleAdapter : ListAdapter<com.lelestacia.lelenimexml.core.model.remote.animefull.Title, TitleAdapter.ViewHolder>(DIFF_CALLBACK) {

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
        fun bind(item: com.lelestacia.lelenimexml.core.model.remote.animefull.Title) {
            binding.tvHolderTitle.text =
                binding.root.context.getString(R.string.placeholder_title, item.type)
            binding.tvValueTitle.text = item.title
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<com.lelestacia.lelenimexml.core.model.remote.animefull.Title>() {
            override fun areItemsTheSame(oldItem: com.lelestacia.lelenimexml.core.model.remote.animefull.Title, newItem: com.lelestacia.lelenimexml.core.model.remote.animefull.Title): Boolean =
                oldItem.type == newItem.type

            override fun areContentsTheSame(oldItem: com.lelestacia.lelenimexml.core.model.remote.animefull.Title, newItem: com.lelestacia.lelenimexml.core.model.remote.animefull.Title): Boolean =
                oldItem == newItem
        }
    }
}