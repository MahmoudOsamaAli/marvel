package com.example.marvel.ui.characterDetails.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.marvel.R
import com.example.marvel.callBacks.OnItemClick
import com.example.marvel.databinding.CharacterDetailsItemsRvBinding
import com.example.marvel.model.charactersDetails.ResultsItem
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class CharacterDetailsViewHolder(private val binding: CharacterDetailsItemsRvBinding,private val clickListener: OnItemClick) :
    RecyclerView.ViewHolder(binding.root) {

    private var item: ResultsItem? = null

    init {
        binding.root.setOnClickListener {
            clickListener.onItemClick(item!!)
        }
    }

    fun bind(item: ResultsItem?) {
        if (item != null) {
            showItemData(item)
        }
    }

    private fun showItemData(item: ResultsItem) {
        this.item = item
        if (!item.thumbnail.path.contains("image_not_available")) {
            val url = item.thumbnail.path + "." + item.thumbnail.extension
            val uri = Uri.parse(url)
            Picasso.get()
                .load(uri).into(binding.itemImage, object : Callback {
                    override fun onSuccess() {
                        binding.animator.displayedChild = 1
                    }

                    override fun onError(e: java.lang.Exception?) {
                        binding.animator.displayedChild = 1
                        Picasso.get().load(R.drawable.image_placeholder).into(binding.itemImage)
                        e?.printStackTrace()
                    }
                })
        }else binding.animator.displayedChild = 1
        binding.itemName.text = item.title.trim()
    }

    companion object {
        fun create(parent: ViewGroup, clickListener: OnItemClick): CharacterDetailsViewHolder {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.character_details_items_rv, parent, false)
            val binding = CharacterDetailsItemsRvBinding.bind(view)
            return CharacterDetailsViewHolder(binding,clickListener)
        }
    }
}
