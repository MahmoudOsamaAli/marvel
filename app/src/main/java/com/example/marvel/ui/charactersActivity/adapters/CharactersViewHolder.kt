package com.example.marvel.ui.charactersActivity.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marvel.R
import com.example.marvel.callBacks.OnCharacterClicked
import com.example.marvel.databinding.CharacterItemBinding
import com.example.marvel.model.characters.ResultsItem
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class CharactersViewHolder(private val binding: CharacterItemBinding, private val clickListener: OnCharacterClicked) :
    RecyclerView.ViewHolder(binding.root) {

    private lateinit var item: ResultsItem

    init {
        binding.root.setOnClickListener {
            clickListener.onItemClicked(item,binding.characterImage)
        }
    }

    fun bind(item: ResultsItem?) {
        if (item != null) {
            showRepoData(item)
        }
    }

    private fun showRepoData(item: ResultsItem) {
        this.item = item
        binding.characterName.text = item.name.trim()
        if (!item.thumbnail.path.contains("image_not_available")) {
            val url = item.thumbnail.path + "." + item.thumbnail.extension
            val uri = Uri.parse(url)
            Picasso.get().load(uri).into(binding.characterImage, object : Callback {
                override fun onSuccess() {
                    binding.animator.displayedChild = 1
                }

                override fun onError(e: java.lang.Exception?) {
                    binding.animator.displayedChild = 1
                    Picasso.get().load(R.drawable.image_placeholder).into(binding.characterImage)
                    e?.printStackTrace()
                }
            })
        } else {
            binding.animator.displayedChild = 1
            Picasso.get().load(R.drawable.image_placeholder).into(binding.characterImage)
        }
    }

    companion object {
        fun create(parent: ViewGroup, clickListener: OnCharacterClicked): CharactersViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.character_item, parent, false)
            val binding = CharacterItemBinding.bind(view)
            return CharactersViewHolder(binding, clickListener)
        }
    }
}
