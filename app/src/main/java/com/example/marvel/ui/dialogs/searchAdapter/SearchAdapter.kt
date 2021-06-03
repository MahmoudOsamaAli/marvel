package com.example.marvel.ui.dialogs.searchAdapter

import android.content.Context
import android.net.Uri
import android.text.Spannable
import android.text.SpannableString
import android.text.style.BackgroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.marvel.R
import com.example.marvel.callBacks.OnCharacterClicked
import com.example.marvel.databinding.SearchItemRvBinding
import com.example.marvel.model.characters.ResultsItem
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.util.*
import kotlin.collections.ArrayList

class SearchAdapter(list: ArrayList<ResultsItem>, private val listener: OnCharacterClicked) :
    RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {

    private var data = ArrayList(list)
    private var filteredList = arrayListOf<ResultsItem>()
    private var searchText: String? = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_item_rv, parent, false)
        val binding = SearchItemRvBinding.bind(itemView)
        return MyViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) =
        holder.bind(filteredList[position])

    override fun getItemCount(): Int = filteredList.size

    fun updateData(list: ArrayList<ResultsItem>) {
        this.data = ArrayList(list)
        this.filteredList = ArrayList(data)
        notifyDataSetChanged()
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                try {
                    searchText = if (charSequence.toString().trim().isEmpty()) {
                        filteredList.clear()
                        ""
                    } else {
                        filteredList.clear()
                        filteredList.addAll(data.filter {
                            it.name.contains(
                                charSequence.toString().trim()
                            )
                        })
                        charSequence.toString().toLowerCase(Locale.ROOT).trim()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                Log.i(TAG, "performFiltering: ${filteredList.size}")
                val filterResults = FilterResults()
                filterResults.values = filteredList
                return filterResults
            }

            override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
                try {
                    filteredList = filterResults.values as ArrayList<ResultsItem>
                    notifyDataSetChanged()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    inner class MyViewHolder(
        private val binding: SearchItemRvBinding,
        private val listener: OnCharacterClicked
    ) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ResultsItem) {

            if (!searchText.isNullOrEmpty()) decorateText(item.name)
            else binding.characterName.text = item.name.trim()

            if (!item.thumbnail.path.contains("image_not_available")) {
                val url = item.thumbnail.path + "." + item.thumbnail.extension
                loadImage(url)
            } else {
                Picasso.get().load(R.drawable.image_placeholder).into(binding.characterImage)
            }

            binding.itemParent.setOnClickListener {
                listener.onItemClicked(
                    filteredList[bindingAdapterPosition],
                    binding.characterImage
                )
            }
        }

        private fun loadImage(url: String) {
            val uri = Uri.parse(url)
            Picasso.get().load(uri).into(binding.characterImage, object : Callback {
                override fun onSuccess() {
                }

                override fun onError(e: java.lang.Exception?) {
                    Picasso.get().load(R.drawable.image_placeholder)
                        .into(binding.characterImage)
                    e?.printStackTrace()
                }
            })
        }

        private fun decorateText(characterNameText: String) {
            val spannable = SpannableString(characterNameText)
            val name = characterNameText.toLowerCase(Locale.ROOT)
            val words = searchText?.split(" ")
            Log.i(TAG, "decorateText: $words")
            words?.forEach {
                if (name.contains(it)) {
                    for (i in 0..name.length step it.length) {
                        val index = name.indexOf(it[0], i)
                        if (i + it.length <= name.length && index != -1 && index + it.length <= name.length) {
                            val b = name.substring(index, index + it.length) == it
                            if (b) highlightWord(spannable, index, it.length, binding.root.context)
                        }
                    }
                }
            }
            binding.characterName.text = spannable
        }

        private fun highlightWord(spannable: Spannable, index: Int, length: Int, context: Context) {
            spannable.setSpan(
                BackgroundColorSpan(ContextCompat.getColor(context, R.color.colorAccent)),
                index,
                index + length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    companion object {
        private const val TAG = "SearchAdapter"
    }

}