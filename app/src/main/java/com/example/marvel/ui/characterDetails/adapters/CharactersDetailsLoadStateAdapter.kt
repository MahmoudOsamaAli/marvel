package com.example.marvel.ui.characterDetails.adapters

import android.view.ViewGroup
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter

class CharactersDetailsLoadStateAdapter(private val retry: () -> Unit) : LoadStateAdapter<CharactersDetailsLoadStateViewHolder>() {
    override fun onBindViewHolder(holder: CharactersDetailsLoadStateViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): CharactersDetailsLoadStateViewHolder {
        return CharactersDetailsLoadStateViewHolder.create(parent, retry)
    }
}