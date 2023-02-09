package com.lelestacia.lelenimexml.feature.common.adapter

import android.os.SystemClock
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.lelestacia.lelenimexml.core.model.character.Character
import com.lelestacia.lelenimexml.feature.common.databinding.ItemCardCharacterBinding

class CharacterAdapter(val onCharacterSelected: (Int) -> Unit) :
    ListAdapter<Character, CharacterAdapter.ViewHolder>(DIFF_CALLBACK) {

    inner class ViewHolder(private val binding: ItemCardCharacterBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Character) {
            var lastClickTime = 0L
            binding.apply {
                ivCharacterImage.load(item.images)
                root.setOnClickListener {
                    if (SystemClock.elapsedRealtime() - lastClickTime < 500) return@setOnClickListener

                    lastClickTime = SystemClock.elapsedRealtime()
                    onCharacterSelected(item.malID)
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
                oldItem.malID == newItem.malID

            override fun areContentsTheSame(
                oldItem: Character,
                newItem: Character
            ): Boolean =
                oldItem == newItem
        }
    }
}
