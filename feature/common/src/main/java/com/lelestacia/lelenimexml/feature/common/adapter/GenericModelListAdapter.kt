package com.lelestacia.lelenimexml.feature.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.lelestacia.lelenimexml.core.model.GenericModel
import com.lelestacia.lelenimexml.feature.common.databinding.ItemGenericModelBinding

class GenericModelListAdapter :
    ListAdapter<GenericModel, GenericModelListAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ItemGenericModelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GenericModel) {
            binding.apply {
                tvTitle.text = item.title
                ivCover.load(item.images)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemGenericModelBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<GenericModel>() {
            override fun areItemsTheSame(oldItem: GenericModel, newItem: GenericModel): Boolean =
                oldItem.malID == newItem.malID

            override fun areContentsTheSame(oldItem: GenericModel, newItem: GenericModel): Boolean =
                oldItem == newItem
        }
    }
}