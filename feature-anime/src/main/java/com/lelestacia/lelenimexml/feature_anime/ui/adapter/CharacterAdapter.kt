package com.lelestacia.lelenimexml.feature_anime.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.lelestacia.lelenimexml.feature_anime.databinding.ItemCardCharacterBinding
import com.lelestacia.lelenimexml.feature_anime.domain.model.CharacterData

class CharacterAdapter : ListAdapter<CharacterData, CharacterAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ItemCardCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CharacterData) {
            binding.apply {
                ivCharacterImage.load(item.images)
                tvCharacterImage.text = item.name
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCardCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<CharacterData>() {
            override fun areItemsTheSame(oldItem: CharacterData, newItem: CharacterData): Boolean =
                oldItem.characterMalId == newItem.characterMalId

            override fun areContentsTheSame(
                oldItem: CharacterData,
                newItem: CharacterData
            ): Boolean =
                oldItem == newItem
        }
    }
}