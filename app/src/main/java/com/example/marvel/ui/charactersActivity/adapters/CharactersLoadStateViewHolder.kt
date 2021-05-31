package com.example.marvel.ui.charactersActivity.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.recyclerview.widget.RecyclerView
import com.example.marvel.R
import com.example.marvel.databinding.CharactersLoadStateFooterViewItemBinding

class CharactersLoadStateViewHolder(
    private val binding: CharactersLoadStateFooterViewItemBinding,
    retry: () -> Unit,
    private val context: Context
) : RecyclerView.ViewHolder(binding.root) {

    init {
        binding.retryButton.setOnClickListener { retry.invoke() }
    }

    fun bind(loadState: LoadState) {
        if (loadState is LoadState.Error) {
            binding.errorMsg.text = context.resources.getString(R.string.error_loading)
        }
        binding.progressBar.isVisible = loadState is LoadState.Loading
        binding.retryButton.isVisible = loadState is LoadState.Error
        binding.errorMsg.isVisible = loadState is LoadState.Error
    }

    companion object {
        fun create(parent: ViewGroup, retry: () -> Unit): CharactersLoadStateViewHolder {
            val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.characters_load_state_footer_view_item, parent, false)
            val binding = CharactersLoadStateFooterViewItemBinding.bind(view)
            return CharactersLoadStateViewHolder(binding, retry,view.context)
        }
    }
}