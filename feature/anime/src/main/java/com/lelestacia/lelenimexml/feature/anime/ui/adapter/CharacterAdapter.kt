package com.lelestacia.lelenimexml.feature.anime.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.lelestacia.lelenimexml.feature.anime.databinding.ItemCardCharacterBinding
import com.lelestacia.lelenimexml.core.model.domain.character.Character

class CharacterAdapter(val onCharacterSelected: (Int) -> Unit) :
    ListAdapter<Character, CharacterAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ItemCardCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Character) {
            binding.apply {
                ivCharacterImage.load(item.images)
                root.setOnClickListener {
                    onCharacterSelected(item.characterMalId)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCardCharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        getItem(position)?.let {
            holder.bind(it)
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Character>() {
            override fun areItemsTheSame(oldItem: Character, newItem: Character): Boolean =
                oldItem.characterMalId == newItem.characterMalId

            override fun areContentsTheSame(
                oldItem: Character,
                newItem: Character
            ): Boolean =
                oldItem == newItem
        }
    }
}