package com.lelestacia.lelenimexml.feature.common.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.imageLoader
import coil.request.CachePolicy
import coil.request.ImageRequest
import coil.size.Scale
import com.lelestacia.lelenimexml.core.model.GenericModel
import com.lelestacia.lelenimexml.feature.common.R
import com.lelestacia.lelenimexml.feature.common.databinding.ItemGenericModelBinding
import kotlinx.coroutines.Dispatchers

class GenericModelListAdapter :
    ListAdapter<GenericModel, GenericModelListAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ItemGenericModelBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GenericModel) {
            val context = binding.root.context
            val imageRequest: ImageRequest =
                ImageRequest.Builder(context = context)
                    .data(data = item.images)
                    .scale(Scale.FIT)
                    .placeholder(drawableResId = R.drawable.placeholder)
                    .crossfade(enable = true)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .fetcherDispatcher(Dispatchers.IO)
                    .transformationDispatcher(Dispatchers.Main)
                    .diskCacheKey("img-cache-${item.malID}")
                    .target(imageView = binding.ivCover)
                    .build()
            context.imageLoader.enqueue(request = imageRequest)
            binding.tvTitle.text = item.title
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