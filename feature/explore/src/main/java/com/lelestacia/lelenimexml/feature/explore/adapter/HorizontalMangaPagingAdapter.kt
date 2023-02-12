package com.lelestacia.lelenimexml.feature.explore.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.lelestacia.lelenimexml.core.model.manga.Manga
import com.lelestacia.lelenimexml.feature.common.databinding.ItemGenericModelBinding

class HorizontalMangaPagingAdapter(private val onClicked: (Manga) -> Unit) :
    PagingDataAdapter<Manga, HorizontalMangaPagingAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ItemGenericModelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Manga) {
            binding.apply {
                tvTitle.text = item.title
                ivCover.apply {
                    load(item.images)
                    setOnClickListener {
                        onClicked(item)
                    }
                }
            }
        }
    }

    override fun onBindViewHolder(holder: HorizontalMangaPagingAdapter.ViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): HorizontalMangaPagingAdapter.ViewHolder {
        return ViewHolder(
            ItemGenericModelBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Manga>() {
            override fun areItemsTheSame(oldItem: Manga, newItem: Manga): Boolean =
                oldItem.malID == newItem.malID

            override fun areContentsTheSame(oldItem: Manga, newItem: Manga): Boolean =
                oldItem == newItem
        }
    }
}