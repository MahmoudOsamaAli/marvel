package com.example.marvel.ui.charactersActivity.adapters

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.marvel.callBacks.OnCharacterClicked
import com.example.marvel.model.characters.ResultsItem

class CharactersAdapter(private val clickListener: OnCharacterClicked) : PagingDataAdapter<ResultsItem, CharactersViewHolder>(CHARACTER_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CharactersViewHolder {
        return CharactersViewHolder.create(parent,clickListener)
    }

    override fun onBindViewHolder(holder: CharactersViewHolder, position: Int) {
        val repoItem = getItem(position)
        if (repoItem != null) {
            holder.bind(repoItem)
        }
    }

    companion object {
        private val CHARACTER_COMPARATOR = object : DiffUtil.ItemCallback<ResultsItem>() {
            override fun areItemsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ResultsItem, newItem: ResultsItem): Boolean =
                oldItem == newItem
        }
    }
}
